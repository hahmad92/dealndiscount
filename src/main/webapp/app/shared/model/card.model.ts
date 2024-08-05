import { IBank } from 'app/shared/model/bank.model';
import { CardCategory } from 'app/shared/model/enumerations/card-category.model';
import { CardNetwork } from 'app/shared/model/enumerations/card-network.model';

export interface ICard {
  id?: number;
  cardName?: string;
  category?: keyof typeof CardCategory;
  cardNetwork?: keyof typeof CardNetwork;
  bank?: IBank | null;
}

export const defaultValue: Readonly<ICard> = {};
