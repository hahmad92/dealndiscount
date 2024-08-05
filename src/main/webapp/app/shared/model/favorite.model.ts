import { FavoriteType } from 'app/shared/model/enumerations/favorite-type.model';

export interface IFavorite {
  id?: number;
  userId?: string;
  storeId?: string | null;
  cityId?: string | null;
  cardId?: string | null;
  favoriteType?: keyof typeof FavoriteType;
}

export const defaultValue: Readonly<IFavorite> = {};
