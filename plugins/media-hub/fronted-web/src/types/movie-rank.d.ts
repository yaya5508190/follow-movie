export type MovieInfo= {
  title: string;
  coverPic: string;
  uri: string;
  rating: number;
  desc: string;
  releaseDate: string;
}

export type MovieRankRsp={
  records: MovieInfo[];
  pageNum: number;
  pageSize: number;
  total: number;
}

export type MovieCacheEntry = {
  records: MovieInfo[]
  pageNum: number
  total: number
}

export type MovieCache = Record<string, MovieCacheEntry>
