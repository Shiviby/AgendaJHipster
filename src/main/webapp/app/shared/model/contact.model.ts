import { IPhoneContact } from 'app/shared/model/phone-contact.model';
import { IEmailContact } from 'app/shared/model/email-contact.model';
import { IAppointment } from 'app/shared/model/appointment.model';

export interface IContact {
  id?: number;
  title?: string;
  avatarContentType?: string | null;
  avatar?: string | null;
  phoneContacts?: IPhoneContact[] | null;
  emailContacts?: IEmailContact[] | null;
  appointments?: IAppointment[] | null;
}

export const defaultValue: Readonly<IContact> = {};
