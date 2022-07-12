import dayjs from 'dayjs';
import { IContact } from 'app/shared/model/contact.model';

export interface IAppointment {
  id?: number;
  title?: string;
  startDate?: string;
  contacts?: IContact[] | null;
}

export const defaultValue: Readonly<IAppointment> = {};
