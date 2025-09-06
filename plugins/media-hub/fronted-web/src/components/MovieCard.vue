<template>
  <div @click="openLink" style="cursor: pointer;">
    <v-hover v-slot="{ isHovering, props: hoverProps }">
      <v-card
        v-bind="hoverProps"
        class="movie-card responsive-card-height rounded-lg"
        :class="{ 'is-hovering': isHovering }"
        elevation="0"
      >
        <v-img
          :src="imageUrl"
          class="align-end rounded-lg"
          :gradient="isHovering ? `to bottom, rgba(0,0,0,.1), rgba(0,0,0,.9)` : undefined"
          height="100%"
          cover
        >
          <template #placeholder>
            <v-row
              class="fill-height ma-0"
              align="center"
              justify="center"
            >
              <v-progress-circular
                indeterminate
                color="grey-lighten-5"
              ></v-progress-circular>
            </v-row>
          </template>

          <!-- This div will contain all text content for positioning -->
          <div v-if="isHovering" class="pa-4 text-white text-with-shadow">
              <v-card-title class="pa-0 pb-1">{{ name }}</v-card-title>
              <v-card-subtitle class="pa-0 d-flex align-center">
                <v-icon color="amber" icon="mdi-star" size="small" class="mr-1"></v-icon>
                <span>{{ rating }}</span>
              </v-card-subtitle>

              <div :class="['description-container', { 'is-hovering': isHovering }]">
                <!-- pt-2 has been removed from here -->
                <p class="pa-0 ma-0 description-text">
                  {{ description }}
                </p>
              </div>
          </div>

        </v-img>
      </v-card>
    </v-hover>
    <div class="pt-2">
      <div class="text-subtitle-2 text-truncate font-weight-bold">{{ name }}</div>
      <div class="d-flex align-center text-caption">
        <v-icon color="amber" icon="mdi-star" size="x-small" class="mr-1"></v-icon>
        <span>{{ rating }}</span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
const props = defineProps<{
  imageUrl: string
  name: string
  rating: number | string
  description: string
  uri: string
}>()

function openLink() {
  if (typeof window !== 'undefined') {
    window.open(props.uri, '_blank')
  }
}
</script>

<style scoped>
.movie-card {
  width: 100%;
  overflow: hidden; /* Contain image zoom */
  transition: transform 0.2s ease-in-out;
}

.movie-card.is-hovering {
  transform: scale(1.05);
  z-index: 10;
}

.text-with-shadow {
  text-shadow: 1px 1px 3px rgba(0,0,0,0.8);
}

/* New responsive height styles */
.responsive-card-height {
  height: 230px; /* Default (xs) */
}
/* sm */
@media (min-width: 600px) {
  .responsive-card-height {
    height: 230px;
  }
}

@media (min-width: 780px) {
  .responsive-card-height {
    height: 300px;
  }
}

/* md */
@media (min-width: 960px) {
  .responsive-card-height {
    height: 250px;
  }
}
/* lg */
@media (min-width: 1280px) {
  .responsive-card-height {
    height: 320px;
  }
}

@media (min-width: 1580px) {
  .responsive-card-height {
    height: 380px;
  }
}

/* xl */
@media (min-width: 1920px) {
  .responsive-card-height {
    height: 210px;
  }
}

/* xl */
@media (min-width: 2160px) {
  .responsive-card-height {
    height: 260px;
  }
}

@media (min-width: 2560px) {
  .responsive-card-height {
    height: 300px;
  }
}

.description-container {
  max-height: 0;
  overflow: hidden;
  transition: max-height 0.3s ease-in-out, margin-top 0.3s ease-in-out;
  margin-top: 0;
}

.description-container.is-hovering {
  max-height: 4.2em; /* Must match .description-text max-height */
  margin-top: 8px; /* Equivalent to pt-2, creates space on hover */
}

.description-text {
  display: -webkit-box;
  -webkit-line-clamp: 3; /* Limit to 3 lines */
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
  line-height: 1.4em;
  max-height: 4.2em; /* 1.4 * 3 */
}
</style>

