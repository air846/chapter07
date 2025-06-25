<template>
  <span class="split-text" ref="textRef">
    <span
      v-for="(char, index) in chars"
      :key="index"
      class="split-char"
      :style="{ animationDelay: `${index * delay}ms` }"
      :class="{ animate: isAnimating }"
    >
      {{ char === ' ' ? '\u00A0' : char }}
    </span>
  </span>
</template>

<script>
import { ref, computed, onMounted, watch } from 'vue'

export default {
  name: 'SplitText',
  props: {
    text: {
      type: String,
      required: true
    },
    delay: {
      type: Number,
      default: 50
    },
    autoPlay: {
      type: Boolean,
      default: true
    },
    trigger: {
      type: Boolean,
      default: false
    }
  },
  emits: ['animation-complete'],
  setup(props, { emit }) {
    const textRef = ref(null)
    const isAnimating = ref(false)

    const chars = computed(() => {
      return props.text.split('')
    })

    const startAnimation = () => {
      isAnimating.value = true
      
      // 计算动画完成时间
      const totalDuration = chars.value.length * props.delay + 600 // 600ms是单个字符动画时间
      
      setTimeout(() => {
        emit('animation-complete')
      }, totalDuration)
    }

    const resetAnimation = () => {
      isAnimating.value = false
    }

    watch(() => props.trigger, (newVal) => {
      if (newVal) {
        startAnimation()
      }
    })

    watch(() => props.text, () => {
      resetAnimation()
      if (props.autoPlay) {
        setTimeout(startAnimation, 100)
      }
    })

    onMounted(() => {
      if (props.autoPlay) {
        setTimeout(startAnimation, 100)
      }
    })

    return {
      textRef,
      chars,
      isAnimating,
      startAnimation,
      resetAnimation
    }
  }
}
</script>

<style lang="scss" scoped>
.split-text {
  display: inline-block;
}

.split-char {
  display: inline-block;
  opacity: 0;
  transform: translateY(20px);
  transition: all 0.3s ease;
  
  &.animate {
    animation: charFadeIn 0.6s ease forwards;
  }
}

@keyframes charFadeIn {
  from {
    opacity: 0;
    transform: translateY(20px) scale(0.8);
  }
  to {
    opacity: 1;
    transform: translateY(0) scale(1);
  }
}
</style>
