<template>
  <a-layout-header class="navbar">
    <div class="logo">AFang AI学习平台</div>
    <a-menu
      v-model:selectedKeys="selectedKeys"
      mode="horizontal"
      :style="{ lineHeight: '64px', border: 'none' }"
    >
      <a-menu-item key="learning-center">
        <router-link to="/learning-center">AI学习中心</router-link>
      </a-menu-item>
      <a-menu-item key="qa-system">
        <router-link to="/qa-system">智能问答系统</router-link>
      </a-menu-item>
      <a-menu-item key="tools">
        <router-link to="/tools">工具资源库</router-link>
      </a-menu-item>
      <a-menu-item key="projects">
        <router-link to="/projects">开源项目广场</router-link>
      </a-menu-item>
    </a-menu>

    <!-- 用户信息区域 -->
    <div class="user-section">
      <template v-if="userStore.isLoggedIn && userStore.userInfo">
        <a-dropdown>
          <div class="user-info">
            <a-avatar :src="userStore.userInfo?.avatar" :size="32">
              {{ userStore.userInfo?.username?.charAt(0).toUpperCase() || 'U' }}
            </a-avatar>
            <span class="username">{{ userStore.userInfo?.username }}</span>
          </div>
          <template #overlay>
            <a-menu>
              <a-menu-item key="profile" @click="router.push('/profile')">
                <UserOutlined />
                个人中心
              </a-menu-item>
              <a-menu-divider />
              <a-menu-item key="logout" @click="handleLogout">
                <LogoutOutlined />
                退出登录
              </a-menu-item>
            </a-menu>
          </template>
        </a-dropdown>
      </template>
      <template v-else>
        <a-button type="primary" @click="router.push('/login')">
          登录
        </a-button>
      </template>
    </div>
  </a-layout-header>
</template>

<script setup lang="ts">
import { ref, watch, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { UserOutlined, LogoutOutlined } from '@ant-design/icons-vue'
import { useUserStore } from '@/stores/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const selectedKeys = ref<string[]>([])

// 根据路由路径设置选中的菜单项
const updateSelectedKeys = () => {
  const path = route.path
  if (path === '/learning-center') {
    selectedKeys.value = ['learning-center']
  } else if (path === '/qa-system') {
    selectedKeys.value = ['qa-system']
  } else if (path === '/tools') {
    selectedKeys.value = ['tools']
  } else if (path === '/projects') {
    selectedKeys.value = ['projects']
  } else {
    selectedKeys.value = []
  }
}

// 退出登录
const handleLogout = () => {
  userStore.logout()
  message.success('退出登录成功')
  router.push('/login')
}

// 初始化用户信息
onMounted(() => {
  userStore.initUser()
})

// 监听路由变化
watch(() => route.path, updateSelectedKeys, { immediate: true })
</script>

<style scoped>
.navbar {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  width: 100%;
  display: flex;
  align-items: center;
  background: #001529;
  padding: 0 20px;
  z-index: 1000;
  height: 64px;
  box-sizing: border-box;
}

.logo {
  color: #fff;
  font-size: 18px;
  font-weight: bold;
  margin-right: 20px;
  white-space: nowrap;
  flex-shrink: 0;
}

.navbar :deep(.ant-menu) {
  flex: 1;
  background: #001529;
  min-width: 0;
}

.navbar :deep(.ant-menu-item) {
  color: rgba(255, 255, 255, 0.65);
}

.navbar :deep(.ant-menu-item:hover) {
  color: #fff;
}

.navbar :deep(.ant-menu-item-selected) {
  color: #fff;
  background-color: #1890ff;
}

.navbar :deep(.ant-menu-item a) {
  color: inherit;
  text-decoration: none;
}

.user-section {
  margin-left: 20px;
  flex-shrink: 0;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 4px 12px;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.user-info:hover {
  background-color: rgba(255, 255, 255, 0.1);
}

.username {
  color: #fff;
  font-size: 14px;
  white-space: nowrap;
}

/* 响应式布局 */
@media screen and (max-width: 992px) {
  .navbar {
    padding: 0 15px;
  }

  .logo {
    font-size: 16px;
    margin-right: 15px;
  }

  .navbar :deep(.ant-menu-item) {
    padding: 0 10px;
  }
}

@media screen and (max-width: 768px) {
  .navbar {
    padding: 0 10px;
  }

  .logo {
    font-size: 14px;
    margin-right: 10px;
  }

  .username {
    display: none;
  }

  .navbar :deep(.ant-menu-item) {
    padding: 0 8px;
    font-size: 13px;
  }
}

@media screen and (max-width: 576px) {
  .logo {
    font-size: 12px;
  }

  .navbar :deep(.ant-menu-item) {
    padding: 0 5px;
    font-size: 12px;
  }
}
</style>
