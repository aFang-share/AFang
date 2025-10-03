<template>
  <div class="learning-center">
    <a-page-header title="AI学习中心" sub-title="探索AI知识，提升技能水平" />

    <div class="content-wrapper">
      <a-row :gutter="16" class="filter-row">
        <a-col :span="18">
          <a-input-search
            v-model:value="searchText"
            placeholder="搜索课程、教程..."
            size="large"
            @search="handleSearch"
          />
        </a-col>
        <a-col :span="6">
          <a-select
            v-model:value="selectedCategory"
            size="large"
            style="width: 100%"
            placeholder="选择分类"
            @change="handleCategoryChange"
          >
            <a-select-option value="">全部分类</a-select-option>
            <a-select-option value="ml">机器学习</a-select-option>
            <a-select-option value="dl">深度学习</a-select-option>
            <a-select-option value="nlp">自然语言处理</a-select-option>
            <a-select-option value="cv">计算机视觉</a-select-option>
          </a-select>
        </a-col>
      </a-row>

      <a-row :gutter="[16, 16]" class="courses-row">
        <a-col
          v-for="course in filteredCourses"
          :key="course.id"
          :xs="24"
          :sm="12"
          :md="8"
          :lg="6"
        >
          <a-card hoverable class="course-card">
            <template #cover>
              <div class="course-cover" :style="{ background: course.color }">
                <span class="course-icon">{{ course.icon }}</span>
              </div>
            </template>
            <a-card-meta :title="course.title" :description="course.description" />
            <div class="course-footer">
              <a-space>
                <a-tag :color="getCategoryColor(course.category)">
                  {{ getCategoryName(course.category) }}
                </a-tag>
                <span class="course-level">{{ course.level }}</span>
              </a-space>
              <a-button type="primary" size="small" @click="viewCourse(course)">
                开始学习
              </a-button>
            </div>
          </a-card>
        </a-col>
      </a-row>

      <a-empty v-if="filteredCourses.length === 0" description="暂无相关课程" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { message } from 'ant-design-vue'

interface Course {
  id: number
  title: string
  description: string
  category: string
  level: string
  icon: string
  color: string
}

const searchText = ref<string>('')
const selectedCategory = ref<string>('')

const courses = ref<Course[]>([
  {
    id: 1,
    title: 'Python机器学习基础',
    description: '掌握机器学习核心算法与应用',
    category: 'ml',
    level: '初级',
    icon: '🤖',
    color: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)'
  },
  {
    id: 2,
    title: '深度学习实战',
    description: '深入理解神经网络与深度学习',
    category: 'dl',
    level: '中级',
    icon: '🧠',
    color: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)'
  },
  {
    id: 3,
    title: 'NLP自然语言处理',
    description: '文本分析与语言模型应用',
    category: 'nlp',
    level: '中级',
    icon: '💬',
    color: 'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)'
  },
  {
    id: 4,
    title: '计算机视觉入门',
    description: '图像识别与处理技术',
    category: 'cv',
    level: '初级',
    icon: '👁️',
    color: 'linear-gradient(135deg, #43e97b 0%, #38f9d7 100%)'
  },
  {
    id: 5,
    title: 'TensorFlow实践',
    description: '使用TensorFlow构建AI应用',
    category: 'dl',
    level: '高级',
    icon: '🔥',
    color: 'linear-gradient(135deg, #fa709a 0%, #fee140 100%)'
  },
  {
    id: 6,
    title: 'PyTorch深度学习',
    description: 'PyTorch框架从入门到精通',
    category: 'dl',
    level: '中级',
    icon: '⚡',
    color: 'linear-gradient(135deg, #30cfd0 0%, #330867 100%)'
  }
])

const filteredCourses = computed(() => {
  return courses.value.filter(course => {
    const matchSearch = !searchText.value ||
      course.title.includes(searchText.value) ||
      course.description.includes(searchText.value)
    const matchCategory = !selectedCategory.value || course.category === selectedCategory.value
    return matchSearch && matchCategory
  })
})

const handleSearch = (value: string) => {
  console.log('Searching for:', value)
}

const handleCategoryChange = (value: string) => {
  console.log('Category changed:', value)
}

const getCategoryName = (category: string): string => {
  const categoryMap: Record<string, string> = {
    ml: '机器学习',
    dl: '深度学习',
    nlp: '自然语言处理',
    cv: '计算机视觉'
  }
  return categoryMap[category] || category
}

const getCategoryColor = (category: string): string => {
  const colorMap: Record<string, string> = {
    ml: 'blue',
    dl: 'purple',
    nlp: 'cyan',
    cv: 'green'
  }
  return colorMap[category] || 'default'
}

const viewCourse = (course: Course) => {
  message.success(`即将开始学习：${course.title}`)
}
</script>

<style scoped>
.learning-center {
  min-height: calc(100vh - 64px);
  background: #f0f2f5;
}

.content-wrapper {
  padding: 24px;
  max-width: 1400px;
  margin: 0 auto;
}

.filter-row {
  margin-bottom: 24px;
}

.courses-row {
  margin-top: 16px;
}

.course-card {
  height: 100%;
  transition: all 0.3s;
}

.course-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.course-cover {
  height: 160px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.course-icon {
  font-size: 64px;
}

.course-footer {
  margin-top: 16px;
  padding-top: 16px;
  border-top: 1px solid #f0f0f0;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.course-level {
  color: #666;
  font-size: 12px;
}
</style>
