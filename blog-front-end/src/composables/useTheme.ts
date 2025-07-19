import { ref, computed } from 'vue'

export type Theme = 'light' | 'dark'

const isDark = ref<boolean>(false)

export const useTheme = () => {
  const theme = computed<Theme>(() => isDark.value ? 'dark' : 'light')
  
  const toggleTheme = () => {
    isDark.value = !isDark.value
    document.documentElement.setAttribute('data-theme', theme.value)
    localStorage.setItem('theme', theme.value)
  }
  
  const initTheme = () => {
    const savedTheme = localStorage.getItem('theme') as Theme
    if (savedTheme) {
      isDark.value = savedTheme === 'dark'
    } else {
      isDark.value = window.matchMedia('(prefers-color-scheme: dark)').matches
    }
    document.documentElement.setAttribute('data-theme', theme.value)
  }
  
  return {
    isDark,
    theme,
    toggleTheme,
    initTheme
  }
}