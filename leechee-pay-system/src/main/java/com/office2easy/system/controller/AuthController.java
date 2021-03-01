package com.office2easy.system.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.office2easy.common.annotation.Log;
import com.office2easy.common.entity.R;
import com.office2easy.common.exception.LCException;
import com.office2easy.common.shiro.JWTToken;
import com.office2easy.common.utils.JwtUtils;
import com.office2easy.domain.system.SysUser;
import com.office2easy.service.system.service.ISysUserService;
import com.office2easy.service.system.vo.AuthVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
public class AuthController {

    private final ISysUserService userService;

    private final DefaultKaptcha producer;

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public AuthController(ISysUserService userService, DefaultKaptcha producer, RedisTemplate<String, Object> redisTemplate) {
        this.userService = userService;
        this.producer = producer;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 用户登陆
     *
     * @return
     */
    @RequestMapping("/login")
    @Log("用户登陆")
    public R login(@RequestBody AuthVo authVo) {
        String code = (String) redisTemplate.opsForValue().get(authVo.getCtoken());
        if (ObjectUtils.isEmpty(code) || !code.equalsIgnoreCase(authVo.getCode())) {
            throw new LCException("验证码错误");
        }
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(authVo, sysUser);
        SysUser user = userService.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, authVo.getUsername()));
        log.info("user:{}", user);
        if (ObjectUtils.isEmpty(user)) {
            throw new LCException("用户不存在");
        }
        String password = authVo.getPassword();
        SimpleHash simpleHash = new SimpleHash("MD5", password, user.getSalt(), 1024);
        if (!simpleHash.toHex().equals(user.getPassword())) {
            throw new LCException("账号或密码错误");
        }
        Subject subject = SecurityUtils.getSubject();
        String token = JwtUtils.sign(authVo.getUsername(), authVo.getPassword());
        JWTToken jwtToken = new JWTToken(token);
        subject.login(jwtToken);
        return R.ok().data("token", token);
    }

    @RequestMapping("/logout")
    public R logout() {
        SecurityUtils.getSubject().logout();
        return R.ok();
    }

    /**
     * 用户注册
     *
     * @param sysUser
     * @param request
     * @return
     */
    @RequestMapping("/register")
    public R register(SysUser sysUser, HttpServletRequest request) {
        sysUser.setLoginIp(request.getRemoteAddr());
        return userService.addUser(sysUser);
    }

    @RequestMapping("/kaptcha")
    public R kaptcha() throws IOException {
        String text = producer.createText();
        BufferedImage image = producer.createImage(text);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", outputStream);
        Map<String, String> map = new HashMap<>();
        String ctoken = UUID.randomUUID().toString().replace("-", "");
        map.put("ctoken", ctoken);
        Base64.Encoder encoder = Base64.getEncoder();
        map.put("image", encoder.encodeToString(outputStream.toByteArray()));
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        ops.set(ctoken, text, 300, TimeUnit.SECONDS);
        return R.ok().data(map);
    }
}
