import './assets/main.css'
import 'ant-design-vue/dist/reset.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'
import Antd from 'ant-design-vue'
import zhCN from 'ant-design-vue/es/locale/zh_CN'
import 'dayjs/locale/zh-cn'

import App from './App.vue'
import router from './router'

const app = createApp(App)

app.use(createPinia())
app.use(router)
app.use(Antd)

// 配置Ant Design Vue为中文
app.provide('$antdLocale', zhCN)

app.mount('#app')
