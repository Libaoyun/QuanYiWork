<template>
  <div>
    <el-form
        v-loading="loading"
        element-loading-text="拼命加载中"
        element-loading-spinner="el-icon-loading"
        element-loading-background="rgba(0, 0, 0, 0.8)"
        :model="loginForm"
        :rules="rules"
        ref="ruleForm"
        class="login-class"
    >
      <h2 class="loginTitle">欢迎登录</h2>
      <el-form-item  prop="username">
        <el-input type="text" v-model="loginForm.username" autocomplete="off" placeholder="用户名"></el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input type="password" v-model="loginForm.password" autocomplete="off" placeholder="密码"></el-input>
      </el-form-item>
      <el-form-item prop="code" >
        <el-input type="text" v-model="loginForm.code"
                  placeholder="点击图片更换验证码"
                  style="width: 238px; margin-right: 10px"
                  @keyup.enter.native="handleLogin('ruleForm')"
        >
        </el-input>
          <img :src="captchaUrl" @click="updateCaptcha" style="position: absolute;" alt="验证码">
      </el-form-item>
      <el-checkbox v-model="checked" class="rememberMe">记住我</el-checkbox>
      <el-button type="primary" style="width: 100%;" @click="handleLogin('ruleForm')">登录</el-button>
    </el-form>
  </div>
</template>

<script>

export default {
  name: "login",
  data() {
    return {
      loginForm: {
        username: 'admin',
        password: '123',
        code: '',
      },
      loading: false,
      //下面验证码请求最好带上当前时间戳参数，否则刷新验证码时可能图片不更新
      //也可以理解为不让后端直接从缓存中调数据，否则不更新，只要参数改变就不会用缓存的内容
      captchaUrl: '/api/captcha?time='+new Date(),
      checked: true,
      rules: {
        //rules中的username等字段不是表单中的字段，而是表格中带有props的引用
        //trigger分blur和change，blur代表失去焦点触发，change代表改变内容触发
        username: [{required: true, message: "请输入用户名!", trigger: 'blur'}],
        password: [{required: true, message: "请输入密码!", trigger: 'blur'}],
        code: [{required: true, message: "请输入验证码!", trigger: 'blur'}],
      }
    }
  },

  created() {
    console.log("进入登录页面!!!!")
  },

  methods: {
    updateCaptcha(){
      //因为图片地址是src路径形式，因此可以直接加上api后请求后台接口
      this.captchaUrl = 'api/captcha?time='+new Date()
    },
    handleLogin(formName){
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.loading = true
          this.postRequest("/api/login", this.loginForm).then(res => {
            this.loading = false
            if (res){
              this.loading = false
              //存储用户token，亲测这里Head(Bearer)后加不加空格都可以！！！
              const tokenStr = res.obj.tokenHead + ' ' + res.obj.token;
              window.sessionStorage.setItem('tokenStr', tokenStr);
              //跳转首页
              let path = this.$route.query.redirect;
              console.log("将要跳转首页或指定地址，已登录")
              //？？？？？？？？这里必须跳转两次才可以，不明觉厉，否则会报异常阻止
              //报Navigation cancelled from "/login" to "/home" with a new navigation.
              this.$router.replace((path === '/login' || path === undefined) ? '/home' : path).catch(() =>
                  this.$router.replace((path === '/login' || path === undefined) ? '/home' : path)
              )
            //  上面是如果一开始就输入了login地址，或者未输入地址是直接跳转过来登录页的，那就默认进入主页，否则登录后就进入之前用户输入的地址

            }
          }).catch(err => {
            console.log(err)
            this.loading = false
          })
        } else {
          this.$message.error("请填写所有字段!")
          return false;
        }
      });
    }
  }
}
</script>

<style scoped>
.login-class{
  border-radius: 15px;
  /*clip样式可以将背景色或图片控制在边框内部/内边距内部/内容内部，多出部分裁剪*/
  background-clip: padding-box;
  width: 350px;
  margin: 160px auto;
  padding: 15px 35px;
  background: #ffffff;
  border: 1px solid #eaeaea;
  box-shadow: 0 0 25px #cac6c6;
}
.rememberMe{
  text-align: left;
  margin: 0 0 10px 0;
}

.loginTitle{
  text-align: center;
  margin: 10px 0 35px 0;
}

/*用于将验证码图片不错位摆放*/
.el-form-item__content{
  display: flex;
  align-items: center;
}


</style>
