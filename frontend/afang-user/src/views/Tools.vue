<template>
  <div class="tools">
    <a-page-header title="工具资源库" sub-title="AI开发必备工具与资源" />

    <div class="content-wrapper">
      <a-row :gutter="16" class="filter-row">
        <a-col :span="18">
          <a-input-search
            v-model:value="searchText"
            placeholder="搜索工具、资源..."
            size="large"
            @search="handleSearch"
          />
        </a-col>
        <a-col :span="6">
          <a-select
            v-model:value="selectedCategory"
            size="large"
            style="width: 100%"
            placeholder="选择类别"
            @change="handleCategoryChange"
          >
            <a-select-option value="">全部类别</a-select-option>
            <a-select-option value="framework">框架库</a-select-option>
            <a-select-option value="dataset">数据集</a-select-option>
            <a-select-option value="visualization">可视化</a-select-option>
            <a-select-option value="notebook">开发环境</a-select-option>
            <a-select-option value="deployment">部署工具</a-select-option>
          </a-select>
        </a-col>
      </a-row>

      <!-- 分类标签 -->
      <div class="category-tabs">
        <a-space wrap :size="8">
          <a-tag
            v-for="cat in categories"
            :key="cat.value"
            :color="selectedCategory === cat.value ? 'blue' : 'default'"
            style="cursor: pointer; font-size: 14px; padding: 4px 12px"
            @click="selectedCategory = cat.value"
          >
            {{ cat.icon }} {{ cat.label }}
          </a-tag>
        </a-space>
      </div>

      <a-row :gutter="[16, 16]" class="tools-row">
        <a-col
          v-for="tool in filteredTools"
          :key="tool.id"
          :xs="24"
          :sm="12"
          :md="8"
          :lg="6"
        >
          <a-card hoverable class="tool-card">
            <div class="tool-icon" :style="{ background: tool.color }">
              <span style="font-size: 40px">{{ tool.icon }}</span>
            </div>
            <a-card-meta :title="tool.name">
              <template #description>
                <div class="tool-description">
                  {{ tool.description }}
                </div>
              </template>
            </a-card-meta>

            <div class="tool-info">
              <a-space direction="vertical" :size="8" style="width: 100%">
                <div class="info-item">
                  <a-tag :color="getCategoryColor(tool.category)">
                    {{ getCategoryName(tool.category) }}
                  </a-tag>
                  <a-tag v-if="tool.popular" color="red">热门</a-tag>
                </div>
                <div class="info-item">
                  <span class="info-label">语言:</span>
                  <span>{{ tool.language }}</span>
                </div>
                <div class="info-item">
                  <span class="info-label">难度:</span>
                  <a-rate :value="tool.difficulty" disabled :count="3" style="font-size: 14px" />
                </div>
              </a-space>
            </div>

            <div class="tool-footer">
              <a-space>
                <a-button type="link" size="small" @click="viewDetails(tool)">
                  详情
                </a-button>
                <a-button type="primary" size="small" @click="useTool(tool)">
                  使用
                </a-button>
              </a-space>
            </div>
          </a-card>
        </a-col>
      </a-row>

      <a-empty v-if="filteredTools.length === 0" description="暂无相关工具" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { message } from 'ant-design-vue'

interface Tool {
  id: number
  name: string
  description: string
  category: string
  icon: string
  color: string
  language: string
  difficulty: number
  popular?: boolean
  url?: string
}

interface Category {
  value: string
  label: string
  icon: string
}

const searchText = ref<string>('')
const selectedCategory = ref<string>('')

const categories: Category[] = [
  { value: '', label: '全部', icon: '📚' },
  { value: 'framework', label: '框架库', icon: '🔧' },
  { value: 'dataset', label: '数据集', icon: '📊' },
  { value: 'visualization', label: '可视化', icon: '📈' },
  { value: 'notebook', label: '开发环境', icon: '💻' },
  { value: 'deployment', label: '部署工具', icon: '🚀' }
]

const tools = ref<Tool[]>([
  {
    id: 1,
    name: 'Jupyter Notebook',
    description: '交互式计算和开发环境，支持多种编程语言',
    category: 'notebook',
    icon: '📓',
    color: 'linear-gradient(135deg, #f97316 0%, #f59e0b 100%)',
    language: 'Python',
    difficulty: 1,
    popular: true
  },
  {
    id: 2,
    name: 'TensorBoard',
    description: 'TensorFlow的可视化工具，用于模型训练监控',
    category: 'visualization',
    icon: '📊',
    color: 'linear-gradient(135deg, #ef4444 0%, #f97316 100%)',
    language: 'Python',
    difficulty: 2
  },
  {
    id: 3,
    name: 'Keras',
    description: '高级神经网络API，简化深度学习模型构建',
    category: 'framework',
    icon: '🧠',
    color: 'linear-gradient(135deg, #ec4899 0%, #ef4444 100%)',
    language: 'Python',
    difficulty: 2,
    popular: true
  },
  {
    id: 4,
    name: 'ImageNet',
    description: '大规模图像数据集，包含1400万张标注图片',
    category: 'dataset',
    icon: '🖼️',
    color: 'linear-gradient(135deg, #06b6d4 0%, #3b82f6 100%)',
    language: '-',
    difficulty: 1
  },
  {
    id: 5,
    name: 'Docker',
    description: '容器化平台，简化应用部署和管理',
    category: 'deployment',
    icon: '🐳',
    color: 'linear-gradient(135deg, #3b82f6 0%, #2563eb 100%)',
    language: 'Shell',
    difficulty: 2,
    popular: true
  },
  {
    id: 6,
    name: 'Weights & Biases',
    description: 'ML实验跟踪和可视化平台',
    category: 'visualization',
    icon: '📉',
    color: 'linear-gradient(135deg, #8b5cf6 0%, #6366f1 100%)',
    language: 'Python',
    difficulty: 2
  },
  {
    id: 7,
    name: 'COCO Dataset',
    description: '目标检测、分割和字幕生成数据集',
    category: 'dataset',
    icon: '🎯',
    color: 'linear-gradient(135deg, #10b981 0%, #059669 100%)',
    language: '-',
    difficulty: 1
  },
  {
    id: 8,
    name: 'FastAPI',
    description: '现代、快速的Web框架，用于构建API',
    category: 'framework',
    icon: '⚡',
    color: 'linear-gradient(135deg, #14b8a6 0%, #10b981 100%)',
    language: 'Python',
    difficulty: 2
  },
  {
    id: 9,
    name: 'Plotly',
    description: '交互式可视化库，创建专业图表',
    category: 'visualization',
    icon: '📈',
    color: 'linear-gradient(135deg, #f59e0b 0%, #eab308 100%)',
    language: 'Python/JS',
    difficulty: 1,
    popular: true
  },
  {
    id: 10,
    name: 'Kubernetes',
    description: '容器编排平台，自动化部署和管理',
    category: 'deployment',
    icon: '☸️',
    color: 'linear-gradient(135deg, #6366f1 0%, #8b5cf6 100%)',
    language: 'YAML',
    difficulty: 3
  },
  {
    id: 11,
    name: 'Hugging Face Datasets',
    description: '海量NLP数据集库，一行代码加载',
    category: 'dataset',
    icon: '🤗',
    color: 'linear-gradient(135deg, #fbbf24 0%, #f59e0b 100%)',
    language: 'Python',
    difficulty: 1,
    popular: true
  },
  {
    id: 12,
    name: 'Ray',
    description: '分布式计算框架，加速AI训练',
    category: 'framework',
    icon: '🌟',
    color: 'linear-gradient(135deg, #a855f7 0%, #ec4899 100%)',
    language: 'Python',
    difficulty: 3
  }
])

const filteredTools = computed(() => {
  return tools.value.filter(tool => {
    const matchSearch = !searchText.value ||
      tool.name.toLowerCase().includes(searchText.value.toLowerCase()) ||
      tool.description.includes(searchText.value)
    const matchCategory = !selectedCategory.value || tool.category === selectedCategory.value
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
  const cat = categories.find(c => c.value === category)
  return cat ? cat.label : category
}

const getCategoryColor = (category: string): string => {
  const colorMap: Record<string, string> = {
    framework: 'blue',
    dataset: 'green',
    visualization: 'orange',
    notebook: 'purple',
    deployment: 'cyan'
  }
  return colorMap[category] || 'default'
}

const viewDetails = (tool: Tool) => {
  message.info(`查看工具详情：${tool.name}`)
}

const useTool = (tool: Tool) => {
  message.success(`即将使用工具：${tool.name}`)
}
</script>

<style scoped>
.tools {
  min-height: calc(100vh - 64px);
  background: #f0f2f5;
}

.content-wrapper {
  padding: 24px;
  max-width: 1400px;
  margin: 0 auto;
}

.filter-row {
  margin-bottom: 16px;
}

.category-tabs {
  margin-bottom: 24px;
  padding: 16px;
  background: white;
  border-radius: 8px;
}

.tools-row {
  margin-top: 16px;
}

.tool-card {
  height: 100%;
  transition: all 0.3s;
}

.tool-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.tool-icon {
  height: 120px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 8px;
  margin-bottom: 16px;
}

.tool-description {
  color: #666;
  font-size: 13px;
  line-height: 1.5;
  margin: 12px 0;
  min-height: 40px;
}

.tool-info {
  margin: 16px 0;
  padding: 12px;
  background: #f9fafb;
  border-radius: 6px;
}

.info-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 13px;
}

.info-label {
  color: #666;
  font-weight: 500;
}

.tool-footer {
  margin-top: 16px;
  padding-top: 12px;
  border-top: 1px solid #f0f0f0;
  display: flex;
  justify-content: flex-end;
}
</style>
