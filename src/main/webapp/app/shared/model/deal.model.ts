import dayjs from 'dayjs';
import { ICard } from 'app/shared/model/card.model';
import { IStore } from 'app/shared/model/store.model';

export interface IDeal {
  id?: number;
  discountPercentage?: number;
  description?: string | null;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  isDealActive?: boolean;
  card?: ICard | null;
  store?: IStore | null;
}

export const defaultValue: Readonly<IDeal> = {
  isDealActive: false,
};
