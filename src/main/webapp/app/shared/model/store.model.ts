import { ICity } from 'app/shared/model/city.model';
import { StoreType } from 'app/shared/model/enumerations/store-type.model';
import { StoreCategory } from 'app/shared/model/enumerations/store-category.model';

export interface IStore {
  id?: number;
  storeName?: string;
  storeType?: keyof typeof StoreType;
  storeCategory?: keyof typeof StoreCategory;
  address?: string;
  phone?: string;
  geoLocation?: string;
  city?: ICity | null;
}

export const defaultValue: Readonly<IStore> = {};
