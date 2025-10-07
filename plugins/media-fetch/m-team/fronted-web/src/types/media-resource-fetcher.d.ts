export type MediaResource = {
  id: string; // 资源ID
  name: string; // 资源名称
  desc: string; // 描述
  images: string[]; // 资源图片
  discount: string; // 资源折扣
  discountEndTime: string; // 资源折扣结束时间
  labels: string[]; // 资源标签
  size: number; // 资源大小
  seeders: number; // 做种数
  leechers: number; // 下载数
  imdb: string; // IMDB地址
  imdbRating: number; // IMDB评分
  douban: string; // 豆瓣地址
  doubanRating: number; // 豆瓣评分
  createdDate: string; // 资源创建时间
}

export type PageResult<T> = {
  pageNum: number;
  pageSize: number;
  total: number;
  records: T[];
}

export type MediaResourcePageReq = {
  keyword?: string;
  pageNum?: number;
  pageSize?: number;
  extra?: {
    categories?: string[];
  };
}
