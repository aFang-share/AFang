<script setup lang="ts">
import { RouterView } from 'vue-router'
import AppNavbar from './components/AppNavbar.vue'
</script>

<template>
  <a-layout style="min-height: 100vh; overflow-x: hidden;">
    <AppNavbar />
    <a-layout-content style="padding: 0; padding-top: 64px;">
      <RouterView />
    </a-layout-content>
  </a-layout>
</template>

<style scoped>
</style>
