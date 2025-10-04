l<template>
  <div class="projects">
    <a-page-header title="开源项目广场" sub-title="发现优质AI开源项目" />

    <div class="content-wrapper">
      <a-row :gutter="16" class="filter-row">
        <a-col :span="14">
          <a-input-search
            v-model:value="searchText"
            placeholder="搜索项目名称、描述..."
            size="large"
            @search="handleSearch"
          />
        </a-col>
        <a-col :span="6">
          <a-select
            v-model:value="selectedTag"
            size="large"
            style="width: 100%"
            placeholder="选择技术栈"
            @change="handleTagChange"
          >
            <a-select-option value="">全部技术</a-select-option>
            <a-select-option value="python">Python</a-select-option>
            <a-select-option value="tensorflow">TensorFlow</a-select-option>
            <a-select-option value="pytorch">PyTorch</a-select-option>
            <a-select-option value="java">Java</a-select-option>
          </a-select>
        </a-col>
        <a-col :span="4">
          <a-select
            v-model:value="sortBy"
            size="large"
            style="width: 100%"
            @change="handleSortChange"
          >
            <a-select-option value="stars">⭐ 按星标</a-select-option>
            <a-select-option value="updated">🕐 按更新</a-select-option>
            <a-select-option value="forks">🍴 按分支</a-select-option>
          </a-select>
        </a-col>
      </a-row>

      <a-row :gutter="[16, 16]" class="projects-row">
        <a-col
          v-for="project in filteredProjects"
          :key="project.id"
          :xs="24"
          :sm="24"
          :md="12"
          :lg="12"
          :xl="8"
        >
          <a-card hoverable class="project-card">
            <div class="project-header">
              <div class="project-title">
                <a-typography-title :level="4">{{ project.name }}</a-typography-title>
                <span class="project-stars">⭐ {{ project.stars }}</span>
              </div>
              <div class="project-description">
                {{ project.description }}
              </div>
            </div>

            <div class="project-tags">
              <a-tag v-for="tag in project.tags" :key="tag" :color="getTagColor(tag)">
                {{ tag }}
              </a-tag>
            </div>

            <a-divider style="margin: 12px 0" />

            <div class="project-stats">
              <a-space :size="16">
                <span><a-typography-text type="secondary">Fork:</a-typography-text> {{ project.forks }}</span>
                <span><a-typography-text type="secondary">Issues:</a-typography-text> {{ project.issues }}</span>
                <span><a-typography-text type="secondary">更新:</a-typography-text> {{ project.lastUpdate }}</span>
              </a-space>
            </div>

            <div class="project-footer">
              <a-space>
                <a-button type="link" size="small" @click="viewProject(project)">
                  查看详情
                </a-button>
                <a-button type="primary" size="small" @click="openGithub(project)">
                  访问GitHub
                </a-button>
              </a-space>
            </div>
          </a-card>
        </a-col>
      </a-row>

      <a-empty v-if="filteredProjects.length === 0" description="暂无相关项目" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { message } from 'ant-design-vue'

interface Project {
  id: number
  name: string
  description: string
  tags: string[]
  stars: number
  forks: number
  issues: number
  lastUpdate: string
  githubUrl: string
}

const searchText = ref<string>('')
const selectedTag = ref<string>('')
const sortBy = ref<string>('stars')

const projects = ref<Project[]>([
  {
    id: 1,
    name: 'TensorFlow',
    description: '一个端到端的开源机器学习平台，提供全面的工具、库和社区资源',
    tags: ['python', 'tensorflow', 'ml'],
    stars: 185000,
    forks: 74000,
    issues: 2340,
    lastUpdate: '2天前',
    githubUrl: 'https://github.com/tensorflow/tensorflow'
  },
  {
    id: 2,
    name: 'PyTorch',
    description: '开源的深度学习框架，提供灵活且高效的深度学习研究平台',
    tags: ['python', 'pytorch', 'dl'],
    stars: 78000,
    forks: 21000,
    issues: 13500,
    lastUpdate: '1天前',
    githubUrl: 'https://github.com/pytorch/pytorch'
  },
  {
    id: 3,
    name: 'Transformers',
    description: 'Hugging Face的自然语言处理库，提供预训练模型和工具',
    tags: ['python', 'nlp', 'pytorch', 'tensorflow'],
    stars: 130000,
    forks: 26000,
    issues: 890,
    lastUpdate: '3小时前',
    githubUrl: 'https://github.com/huggingface/transformers'
  },
  {
    id: 4,
    name: 'OpenCV',
    description: '开源计算机视觉库，提供丰富的图像和视频处理功能',
    tags: ['python', 'cv', 'cpp'],
    stars: 76000,
    forks: 55000,
    issues: 3200,
    lastUpdate: '5天前',
    githubUrl: 'https://github.com/opencv/opencv'
  },
  {
    id: 5,
    name: 'Scikit-learn',
    description: 'Python机器学习库，提供简单高效的数据挖掘和数据分析工具',
    tags: ['python', 'ml'],
    stars: 59000,
    forks: 25000,
    issues: 1650,
    lastUpdate: '1周前',
    githubUrl: 'https://github.com/scikit-learn/scikit-learn'
  },
  {
    id: 6,
    name: 'Stable Diffusion',
    description: '开源的AI图像生成模型，可生成高质量的图像内容',
    tags: ['python', 'pytorch', 'cv'],
    stars: 67000,
    forks: 13000,
    issues: 560,
    lastUpdate: '4天前',
    githubUrl: 'https://github.com/Stability-AI/stablediffusion'
  }
])

const filteredProjects = computed(() => {
  let result = projects.value.filter(project => {
    const matchSearch = !searchText.value ||
      project.name.toLowerCase().includes(searchText.value.toLowerCase()) ||
      project.description.includes(searchText.value)
    const matchTag = !selectedTag.value || project.tags.includes(selectedTag.value)
    return matchSearch && matchTag
  })

  // 排序
  if (sortBy.value === 'stars') {
    result = result.sort((a, b) => b.stars - a.stars)
  } else if (sortBy.value === 'forks') {
    result = result.sort((a, b) => b.forks - a.forks)
  }

  return result
})

const handleSearch = (value: string) => {
  console.log('Searching for:', value)
}

const handleTagChange = (value: string) => {
  console.log('Tag changed:', value)
}

const handleSortChange = (value: string) => {
  console.log('Sort changed:', value)
}

const getTagColor = (tag: string): string => {
  const colorMap: Record<string, string> = {
    python: 'blue',
    tensorflow: 'orange',
    pytorch: 'red',
    ml: 'green',
    dl: 'purple',
    nlp: 'cyan',
    cv: 'geekblue',
    java: 'volcano',
    cpp: 'magenta'
  }
  return colorMap[tag] || 'default'
}

const viewProject = (project: Project) => {
  message.info(`查看项目详情：${project.name}`)
}

const openGithub = (project: Project) => {
  message.success(`即将打开GitHub: ${project.name}`)
  // window.open(project.githubUrl, '_blank')
}
</script>

<style scoped>
.projects {
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

.projects-row {
  margin-top: 16px;
}

.project-card {
  height: 100%;
  transition: all 0.3s;
}

.project-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.project-header {
  margin-bottom: 12px;
}

.project-title {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 8px;
}

.project-title h4 {
  margin: 0;
  flex: 1;
}

.project-stars {
  color: #faad14;
  font-weight: 600;
  font-size: 14px;
  white-space: nowrap;
  margin-left: 12px;
}

.project-description {
  margin-bottom: 0;
  margin-top: 8px;
  color: #666;
  font-size: 14px;
  line-height: 1.6;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}

.project-tags {
  margin-bottom: 8px;
}

.project-stats {
  font-size: 12px;
  color: #666;
}

.project-footer {
  margin-top: 16px;
  padding-top: 12px;
  border-top: 1px solid #f0f0f0;
  display: flex;
  justify-content: flex-end;
}
</style>
