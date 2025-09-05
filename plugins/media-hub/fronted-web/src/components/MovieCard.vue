<template>
  <v-card
    class="mx-auto"
    max-width="400"
  >
    <v-img
      class="align-end text-white"
      height="200"
      :src="imageUrl"
      cover
    >
      <v-card-title>{{ name }}</v-card-title>
    </v-img>

    <v-card-subtitle class="pt-4">
      评分: {{ rating }}
    </v-card-subtitle>

    <v-card-text>
      <div>{{ description }}</div>
    </v-card-text>

    <v-card-actions>
      <v-btn color="orange">
        Share
      </v-btn>

      <v-btn color="orange">
        Explore
      </v-btn>
    </v-card-actions>
  </v-card>
</template>

<script setup lang="ts">
defineProps<{
  imageUrl: string
  name: string
  rating: number | string
  description: string
}>()
</script>
