<template>
  <div class="profile-page">
    <a-page-header title="个人中心" sub-title="管理您的个人信息和学习数据" />

    <div class="content-wrapper">
      <a-row :gutter="24">
        <!-- 左侧：用户信息卡片 -->
        <a-col :xs="24" :md="8">
          <a-card class="user-info-card">
            <div class="avatar-section">
              <a-avatar :src="userStore.userInfo?.avatar" :size="120">
                {{ userStore.userInfo?.username?.charAt(0).toUpperCase() }}
              </a-avatar>
              <a-button type="link" class="change-avatar-btn">更换头像</a-button>
            </div>
            <div class="user-details">
              <h2>{{ userStore.userInfo?.username }}</h2>
              <p class="user-email">
                <MailOutlined />
                {{ userStore.userInfo?.email || '未设置邮箱' }}
              </p>
              <p class="user-id">
                <IdcardOutlined />
                ID: {{ userStore.userInfo?.id }}
              </p>
            </div>
            <a-divider />
            <div class="stats-section">
              <a-row :gutter="16">
                <a-col :span="12">
                  <div class="stat-item">
                    <div class="stat-value">{{ stats.coursesCompleted }}</div>
                    <div class="stat-label">已完成课程</div>
                  </div>
                </a-col>
                <a-col :span="12">
                  <div class="stat-item">
                    <div class="stat-value">{{ stats.studyDays }}</div>
                    <div class="stat-label">学习天数</div>
                  </div>
                </a-col>
              </a-row>
              <a-row :gutter="16" style="margin-top: 16px">
                <a-col :span="12">
                  <div class="stat-item">
                    <div class="stat-value">{{ stats.totalHours }}</div>
                    <div class="stat-label">学习时长(h)</div>
                  </div>
                </a-col>
                <a-col :span="12">
                  <div class="stat-item">
                    <div class="stat-value">{{ stats.projects }}</div>
                    <div class="stat-label">完成项目</div>
                  </div>
                </a-col>
              </a-row>
            </div>
          </a-card>
        </a-col>

        <!-- 右侧：详细信息和设置 -->
        <a-col :xs="24" :md="16">
          <a-card>
            <a-tabs v-model:activeKey="activeTab">
              <!-- 基本信息 -->
              <a-tab-pane key="info" tab="基本信息">
                <a-form
                  :model="profileForm"
                  :label-col="{ span: 6 }"
                  :wrapper-col="{ span: 18 }"
                >
                  <a-form-item label="用户名">
                    <a-input v-model:value="profileForm.username" placeholder="请输入用户名" />
                  </a-form-item>
                  <a-form-item label="邮箱">
                    <a-input v-model:value="profileForm.email" placeholder="请输入邮箱" />
                  </a-form-item>
                  <a-form-item label="手机号">
                    <a-input v-model:value="profileForm.phone" placeholder="请输入手机号" />
                  </a-form-item>
                  <a-form-item label="个人简介">
                    <a-textarea
                      v-model:value="profileForm.bio"
                      :rows="4"
                      placeholder="介绍一下你自己吧..."
                    />
                  </a-form-item>
                  <a-form-item :wrapper-col="{ offset: 6, span: 18 }">
                    <a-space>
                      <a-button type="primary" @click="handleSaveProfile">
                        保存修改
                      </a-button>
                      <a-button @click="handleResetProfile">重置</a-button>
                    </a-space>
                  </a-form-item>
                </a-form>
              </a-tab-pane>

              <!-- 学习记录 -->
              <a-tab-pane key="learning" tab="学习记录">
                <a-list
                  :data-source="learningRecords"
                  :pagination="{ pageSize: 5 }"
                >
                  <template #renderItem="{ item }">
                    <a-list-item>
                      <a-list-item-meta
                        :description="item.date"
                      >
                        <template #title>
                          <a>{{ item.course }}</a>
                        </template>
                        <template #avatar>
                          <a-avatar :style="{ backgroundColor: item.color }">
                            {{ item.icon }}
                          </a-avatar>
                        </template>
                      </a-list-item-meta>
                      <template #actions>
                        <a-progress
                          type="circle"
                          :percent="item.progress"
                          :width="50"
                        />
                      </template>
                    </a-list-item>
                  </template>
                </a-list>
              </a-tab-pane>

              <!-- 账号安全 -->
              <a-tab-pane key="security" tab="账号安全">
                <a-list :data-source="securityItems">
                  <template #renderItem="{ item }">
                    <a-list-item>
                      <a-list-item-meta
                        :title="item.title"
                        :description="item.description"
                      >
                        <template #avatar>
                          <component :is="item.icon" style="font-size: 24px" />
                        </template>
                      </a-list-item-meta>
                      <template #actions>
                        <a-button type="link" @click="handleSecurityAction(item.action)">
                          {{ item.actionText }}
                        </a-button>
                      </template>
                    </a-list-item>
                  </template>
                </a-list>
              </a-tab-pane>

              <!-- 偏好设置 -->
              <a-tab-pane key="preferences" tab="偏好设置">
                <a-form :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
                  <a-form-item label="语言设置">
                    <a-select v-model:value="preferences.language" style="width: 100%">
                      <a-select-option value="zh-CN">简体中文</a-select-option>
                      <a-select-option value="en-US">English</a-select-option>
                    </a-select>
                  </a-form-item>
                  <a-form-item label="主题模式">
                    <a-radio-group v-model:value="preferences.theme">
                      <a-radio value="light">浅色</a-radio>
                      <a-radio value="dark">深色</a-radio>
                      <a-radio value="auto">跟随系统</a-radio>
                    </a-radio-group>
                  </a-form-item>
                  <a-form-item label="邮件通知">
                    <a-switch v-model:checked="preferences.emailNotification" />
                  </a-form-item>
                  <a-form-item label="学习提醒">
                    <a-switch v-model:checked="preferences.learningReminder" />
                  </a-form-item>
                  <a-form-item :wrapper-col="{ offset: 6, span: 18 }">
                    <a-button type="primary" @click="handleSavePreferences">
                      保存设置
                    </a-button>
                  </a-form-item>
                </a-form>
              </a-tab-pane>
            </a-tabs>
          </a-card>
        </a-col>
      </a-row>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import {
  MailOutlined,
  IdcardOutlined,
  LockOutlined,
  SafetyOutlined,
  MobileOutlined,
} from '@ant-design/icons-vue'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
const activeTab = ref('info')

// 统计数据
const stats = reactive({
  coursesCompleted: 12,
  studyDays: 45,
  totalHours: 120,
  projects: 8,
})

// 个人资料表单
const profileForm = reactive({
  username: '',
  email: '',
  phone: '',
  bio: '',
})

// 学习记录
const learningRecords = ref([
  {
    course: 'Python机器学习基础',
    date: '2024-01-15',
    progress: 85,
    icon: '🤖',
    color: '#667eea',
  },
  {
    course: '深度学习实战',
    date: '2024-01-14',
    progress: 60,
    icon: '🧠',
    color: '#f093fb',
  },
  {
    course: 'NLP自然语言处理',
    date: '2024-01-13',
    progress: 45,
    icon: '💬',
    color: '#4facfe',
  },
  {
    course: '计算机视觉入门',
    date: '2024-01-12',
    progress: 100,
    icon: '👁️',
    color: '#43e97b',
  },
  {
    course: 'TensorFlow实践',
    date: '2024-01-10',
    progress: 30,
    icon: '🔥',
    color: '#fa709a',
  },
])

// 安全设置项
const securityItems = ref([
  {
    title: '登录密码',
    description: '定期更换密码可以提高账号安全性',
    icon: LockOutlined,
    action: 'changePassword',
    actionText: '修改',
  },
  {
    title: '双重验证',
    description: '开启双重验证可以更好地保护您的账号',
    icon: SafetyOutlined,
    action: 'twoFactor',
    actionText: '开启',
  },
  {
    title: '绑定手机',
    description: '绑定手机号用于账号找回和验证',
    icon: MobileOutlined,
    action: 'bindPhone',
    actionText: '绑定',
  },
])

// 偏好设置
const preferences = reactive({
  language: 'zh-CN',
  theme: 'light',
  emailNotification: true,
  learningReminder: true,
})

// 初始化表单数据
onMounted(() => {
  if (userStore.userInfo) {
    profileForm.username = userStore.userInfo.username
    profileForm.email = userStore.userInfo.email || ''
  }
})

// 保存个人资料
const handleSaveProfile = () => {
  // TODO: 调用API保存个人资料
  message.success('个人信息保存成功')
}

// 重置个人资料
const handleResetProfile = () => {
  if (userStore.userInfo) {
    profileForm.username = userStore.userInfo.username
    profileForm.email = userStore.userInfo.email || ''
    profileForm.phone = ''
    profileForm.bio = ''
  }
  message.info('已重置为原始信息')
}

// 处理安全操作
const handleSecurityAction = (action: string) => {
  switch (action) {
    case 'changePassword':
      message.info('跳转到修改密码页面')
      break
    case 'twoFactor':
      message.info('开启双重验证')
      break
    case 'bindPhone':
      message.info('绑定手机号')
      break
  }
}

// 保存偏好设置
const handleSavePreferences = () => {
  // TODO: 调用API保存偏好设置
  message.success('偏好设置保存成功')
}
</script>

<style scoped>
.profile-page {
  min-height: calc(100vh - 64px);
  background: #f0f2f5;
}

.content-wrapper {
  padding: 24px;
  max-width: 1400px;
  margin: 0 auto;
}

.user-info-card {
  text-align: center;
  height: 100%;
}

.avatar-section {
  padding: 20px 0;
}

.change-avatar-btn {
  display: block;
  margin: 12px auto 0;
  padding: 0;
}

.user-details {
  margin: 20px 0;
}

.user-details h2 {
  margin: 0 0 16px;
  font-size: 24px;
  font-weight: 600;
  color: #262626;
}

.user-email,
.user-id {
  margin: 8px 0;
  color: #595959;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.stats-section {
  margin-top: 20px;
}

.stat-item {
  text-align: center;
  padding: 12px 0;
}

.stat-value {
  font-size: 28px;
  font-weight: 600;
  color: #1890ff;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 12px;
  color: #8c8c8c;
}

@media screen and (max-width: 768px) {
  .content-wrapper {
    padding: 16px;
  }

  .user-info-card {
    margin-bottom: 16px;
  }
}
</style>
