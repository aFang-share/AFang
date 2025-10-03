<template>
  <div class="login-container">
    <a-card class="login-card" title="用户登录">
      <a-form
        :model="formState"
        :rules="rules"
        @finish="handleLogin"
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

        <a-form-item>
          <a-button
            type="primary"
            html-type="submit"
            size="large"
            block
            :loading="loading"
          >
            登录
          </a-button>
        </a-form-item>

        <div class="login-footer">
          还没有账号？
          <router-link to="/register">立即注册</router-link>
        </div>
      </a-form>
    </a-card>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { UserOutlined, LockOutlined } from '@ant-design/icons-vue'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()

const formState = reactive({
  username: '',
  password: '',
})

const loading = ref(false)

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
}

const handleLogin = async () => {
  loading.value = true
  try {
    // TODO: 这里应该调用实际的登录 API
    // 模拟登录请求
    await new Promise((resolve) => setTimeout(resolve, 1000))

    // 模拟登录成功，设置用户信息
    userStore.login({
      id: '1',
      username: formState.username,
      avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=' + formState.username,
    })

    message.success('登录成功')
    router.push('/learning-center')
  } catch (error) {
    message.error('登录失败，请重试')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  min-height: calc(100vh - 64px);
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
}

.login-card {
  width: 100%;
  max-width: 400px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

.login-card :deep(.ant-card-head) {
  text-align: center;
  font-size: 24px;
  font-weight: bold;
}

.login-footer {
  text-align: center;
  margin-top: 16px;
}

.login-footer a {
  color: #1890ff;
  text-decoration: none;
}

.login-footer a:hover {
  text-decoration: underline;
}
</style>
