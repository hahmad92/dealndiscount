import dayjs from 'dayjs';
import { ICity } from 'app/shared/model/city.model';

export interface IExtendedUser {
  id?: number;
  dob?: dayjs.Dayjs | null;
  city?: ICity | null;
}

export const defaultValue: Readonly<IExtendedUser> = {};
