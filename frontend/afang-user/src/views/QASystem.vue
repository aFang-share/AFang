<template>
  <div class="qa-system">
    <a-page-header title="智能问答系统" sub-title="AI助手随时为您解答疑问" />

    <div class="content-wrapper">
      <a-row :gutter="16">
        <!-- 左侧：历史记录 -->
        <a-col :xs="24" :sm="24" :md="6" :lg="6">
          <a-card title="对话历史" class="history-card">
            <template #extra>
              <a-button type="link" size="small" @click="clearHistory">清空</a-button>
            </template>
            <a-list
              :data-source="conversationHistory"
              :locale="{ emptyText: '暂无对话记录' }"
            >
              <template #renderItem="{ item }">
                <a-list-item
                  class="history-item"
                  :class="{ active: item.id === currentConversationId }"
                  @click="loadConversation(item)"
                  style="cursor: pointer"
                >
                  <a-list-item-meta>
                    <template #title>
                      <div style="overflow: hidden; text-overflow: ellipsis; white-space: nowrap">
                        {{ item.title }}
                      </div>
                    </template>
                    <template #description>
                      <span style="font-size: 12px; color: rgba(0, 0, 0, 0.45)">
                        {{ item.time }}
                      </span>
                    </template>
                  </a-list-item-meta>
                </a-list-item>
              </template>
            </a-list>
          </a-card>
        </a-col>

        <!-- 右侧：聊天界面 -->
        <a-col :xs="24" :sm="24" :md="18" :lg="18">
          <a-card class="chat-card">
            <div class="chat-container">
              <!-- 消息列表 -->
              <div class="messages-container" ref="messagesContainer">
                <a-empty
                  v-if="messages.length === 0"
                  description="开始您的第一个提问吧"
                >
                  <template #description>
                    <p style="color: #999">👋 您好！我是AI助手，有什么可以帮助您的吗？</p>
                  </template>
                </a-empty>

                <div
                  v-for="msg in messages"
                  :key="msg.id"
                  :class="['message-item', msg.role]"
                >
                  <div class="message-avatar">
                    <a-avatar :style="{ backgroundColor: msg.role === 'user' ? '#1890ff' : '#87d068' }">
                      {{ msg.role === 'user' ? '我' : 'AI' }}
                    </a-avatar>
                  </div>
                  <div class="message-content">
                    <div class="message-header">
                      <span class="message-role">{{ msg.role === 'user' ? '您' : 'AI助手' }}</span>
                      <span class="message-time">{{ msg.time }}</span>
                    </div>
                    <div class="message-text">
                      <p style="margin: 0; white-space: pre-wrap; word-break: break-word">
                        {{ msg.content }}
                      </p>
                    </div>
                  </div>
                </div>

                <!-- 加载状态 -->
                <div v-if="isLoading" class="message-item assistant">
                  <div class="message-avatar">
                    <a-avatar style="background-color: #87d068">AI</a-avatar>
                  </div>
                  <div class="message-content">
                    <div class="message-text">
                      <a-spin size="small" /> 正在思考中...
                    </div>
                  </div>
                </div>
              </div>

              <!-- 输入框 -->
              <div class="input-container">
                <a-textarea
                  v-model:value="inputText"
                  placeholder="输入您的问题..."
                  :auto-size="{ minRows: 2, maxRows: 6 }"
                  @pressEnter="handleSend"
                  :disabled="isLoading"
                />
                <div class="input-actions">
                  <a-space>
                    <a-button @click="clearMessages" :disabled="messages.length === 0">
                      清空对话
                    </a-button>
                    <a-button
                      type="primary"
                      @click="handleSend"
                      :loading="isLoading"
                      :disabled="!inputText.trim()"
                    >
                      发送 (Enter)
                    </a-button>
                  </a-space>
                </div>
              </div>
            </div>
          </a-card>
        </a-col>
      </a-row>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, nextTick } from 'vue'
import { message } from 'ant-design-vue'

interface Message {
  id: number
  role: 'user' | 'assistant'
  content: string
  time: string
}

interface Conversation {
  id: number
  title: string
  time: string
  messages: Message[]
}

const inputText = ref<string>('')
const messages = ref<Message[]>([])
const isLoading = ref<boolean>(false)
const messagesContainer = ref<HTMLElement | null>(null)
const currentConversationId = ref<number | null>(null)

const conversationHistory = ref<Conversation[]>([
  {
    id: 1,
    title: '什么是机器学习？',
    time: '2小时前',
    messages: []
  },
  {
    id: 2,
    title: 'Python数据分析入门',
    time: '昨天',
    messages: []
  },
  {
    id: 3,
    title: '深度学习框架对比',
    time: '3天前',
    messages: []
  }
])

let messageIdCounter = 1

const getCurrentTime = (): string => {
  const now = new Date()
  return `${now.getHours()}:${String(now.getMinutes()).padStart(2, '0')}`
}

const scrollToBottom = () => {
  nextTick(() => {
    if (messagesContainer.value) {
      messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
    }
  })
}

const handleSend = async (e?: KeyboardEvent) => {
  // 如果按下的是Shift+Enter，则不发送，允许换行
  if (e && e.shiftKey) {
    return
  }

  if (e) {
    e.preventDefault()
  }

  const text = inputText.value.trim()
  if (!text || isLoading.value) {
    return
  }

  // 添加用户消息
  const userMessage: Message = {
    id: messageIdCounter++,
    role: 'user',
    content: text,
    time: getCurrentTime()
  }
  messages.value.push(userMessage)
  inputText.value = ''
  scrollToBottom()

  // 模拟AI回复
  isLoading.value = true
  setTimeout(() => {
    const aiResponse: Message = {
      id: messageIdCounter++,
      role: 'assistant',
      content: getAIResponse(text),
      time: getCurrentTime()
    }
    messages.value.push(aiResponse)
    isLoading.value = false
    scrollToBottom()
  }, 1500)
}

const getAIResponse = (question: string): string => {
  // 简单的模拟响应
  const responses: Record<string, string> = {
    default: '这是一个很好的问题！作为AI助手，我可以为您提供相关信息和建议。请问您想深入了解哪个方面呢？',
    hello: '您好！很高兴为您服务。我是AFang AI学习平台的智能助手，可以回答关于人工智能、机器学习、深度学习等方面的问题。',
    machine: '机器学习是人工智能的一个分支，它使计算机系统能够从数据中学习并改进，而无需明确编程。常见的机器学习算法包括线性回归、决策树、随机森林、支持向量机等。',
    deep: '深度学习是机器学习的一个子领域，使用多层神经网络来学习数据的复杂表示。它在图像识别、自然语言处理、语音识别等领域取得了突破性进展。'
  }

  const lowerQuestion = question.toLowerCase()
  if (lowerQuestion.includes('你好') || lowerQuestion.includes('hello')) {
    return responses.hello
  } else if (lowerQuestion.includes('机器学习')) {
    return responses.machine
  } else if (lowerQuestion.includes('深度学习')) {
    return responses.deep
  }
  return responses.default
}

const clearMessages = () => {
  messages.value = []
  message.info('对话已清空')
}

const clearHistory = () => {
  conversationHistory.value = []
  message.success('历史记录已清空')
}

const loadConversation = (conversation: Conversation) => {
  currentConversationId.value = conversation.id
  messages.value = [...conversation.messages]
  message.info(`已加载对话：${conversation.title}`)
}
</script>

<style scoped>
.qa-system {
  min-height: calc(100vh - 64px);
  background: #f0f2f5;
}

.content-wrapper {
  padding: 24px;
  max-width: 1400px;
  margin: 0 auto;
}

.history-card {
  height: calc(100vh - 200px);
  overflow-y: auto;
}

.history-item {
  cursor: pointer;
  transition: background-color 0.3s;
  padding: 8px;
  border-radius: 4px;
}

.history-item:hover {
  background-color: #f5f5f5;
}

.history-item.active {
  background-color: #e6f7ff;
  border-left: 3px solid #1890ff;
}

.chat-card {
  height: calc(100vh - 200px);
  display: flex;
  flex-direction: column;
}

.chat-card :deep(.ant-card-body) {
  flex: 1;
  padding: 0;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.chat-container {
  display: flex;
  flex-direction: column;
  height: 100%;
}

.messages-container {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  background: #fafafa;
}

.message-item {
  display: flex;
  margin-bottom: 20px;
  animation: fadeIn 0.3s ease-in;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.message-item.user {
  flex-direction: row-reverse;
}

.message-avatar {
  margin: 0 12px;
}

.message-content {
  max-width: 70%;
  background: white;
  padding: 12px 16px;
  border-radius: 8px;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
}

.message-item.user .message-content {
  background: #1890ff;
  color: white;
}

.message-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 8px;
  font-size: 12px;
}

.message-role {
  font-weight: 600;
}

.message-item.user .message-role,
.message-item.user .message-time {
  color: rgba(255, 255, 255, 0.85);
}

.message-time {
  color: #999;
}

.message-text {
  line-height: 1.6;
}

.input-container {
  padding: 16px;
  background: white;
  border-top: 1px solid #f0f0f0;
}

.input-actions {
  margin-top: 12px;
  display: flex;
  justify-content: flex-end;
}
</style>
