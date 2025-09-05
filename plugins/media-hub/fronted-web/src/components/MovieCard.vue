<template>
  <v-card
    class="movie-card responsive-card-height"
    @click="openLink"
  >
    <v-hover v-slot="{ isHovering, props: hoverProps }">
      <v-img
        :src="imageUrl"
        class="align-end"
        :gradient="`to bottom, rgba(0,0,0,.1), ${isHovering ? 'rgba(0,0,0,.9)' : 'rgba(0,0,0,.5)'}`"
        height="100%"
        cover
        v-bind="hoverProps"
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
        <div class="pa-4 text-white">
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
    </v-hover>
  </v-card>
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
  transition: transform 0.2s ease-in-out;
  overflow: hidden; /* Prevents scaled content from overlapping siblings */
  cursor: pointer;
}
.movie-card:hover {
    transform: scale(1.05);
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
