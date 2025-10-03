<template>
  <div class="register-container">
    <a-card class="register-card" title="用户注册">
      <a-form
        :model="formState"
        :rules="rules"
        @finish="handleRegister"
        layout="vertical"
      >
        <a-form-item label="用户名" name="username">
          <a-input
            v-model:value="formState.username"
            placeholder="请输入用户名"
            size="large"
          >
            <template #prefix>
              <UserOutlined />
            </template>
          </a-input>
        </a-form-item>

        <a-form-item label="邮箱" name="email">
          <a-input
            v-model:value="formState.email"
            placeholder="请输入邮箱"
            size="large"
          >
            <template #prefix>
              <MailOutlined />
            </template>
          </a-input>
        </a-form-item>

        <a-form-item label="密码" name="password">
          <a-input-password
            v-model:value="formState.password"
            placeholder="请输入密码"
            size="large"
          >
            <template #prefix>
              <LockOutlined />
            </template>
          </a-input-password>
        </a-form-item>

        <a-form-item label="确认密码" name="confirmPassword">
          <a-input-password
            v-model:value="formState.confirmPassword"
            placeholder="请再次输入密码"
            size="large"
          >
            <template #prefix>
              <LockOutlined />
            </template>
          </a-input-password>
        </a-form-item>

        <a-form-item>
          <a-button
            type="primary"
            html-type="submit"
            size="large"
            block
            :loading="loading"
          >
            注册
          </a-button>
        </a-form-item>

        <div class="register-footer">
          已有账号？
          <router-link to="/login">立即登录</router-link>
        </div>
      </a-form>
    </a-card>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { UserOutlined, LockOutlined, MailOutlined } from '@ant-design/icons-vue'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()

const formState = reactive({
  username: '',
  email: '',
  password: '',
  confirmPassword: '',
})

const loading = ref(false)

const validatePassword = (_rule: any, value: string) => {
  if (value && value !== formState.password) {
    return Promise.reject('两次输入的密码不一致')
  }
  return Promise.resolve()
}

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' },
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' },
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于 6 个字符', trigger: 'blur' },
  ],
  confirmPassword: [
    { required: true, message: '请再次输入密码', trigger: 'blur' },
    { validator: validatePassword, trigger: 'blur' },
  ],
}

const handleRegister = async () => {
  loading.value = true
  try {
    // TODO: 这里应该调用实际的注册 API
    // 模拟注册请求
    await new Promise((resolve) => setTimeout(resolve, 1000))

    message.success('注册成功，即将跳转到登录页面')

    // 注册成功后跳转到登录页面
    setTimeout(() => {
      router.push('/login')
    }, 1500)
  } catch (error) {
    message.error('注册失败，请重试')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.register-container {
  min-height: calc(100vh - 64px);
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
}

.register-card {
  width: 100%;
  max-width: 400px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

.register-card :deep(.ant-card-head) {
  text-align: center;
  font-size: 24px;
  font-weight: bold;
}

.register-footer {
  text-align: center;
  margin-top: 16px;
}

.register-footer a {
  color: #1890ff;
  text-decoration: none;
}

.register-footer a:hover {
  text-decoration: underline;
}
</style>
