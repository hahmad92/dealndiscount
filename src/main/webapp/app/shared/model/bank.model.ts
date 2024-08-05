import { BankType } from 'app/shared/model/enumerations/bank-type.model';

export interface IBank {
  id?: number;
  bankName?: string;
  bankType?: keyof typeof BankType;
}

export const defaultValue: Readonly<IBank> = {};
