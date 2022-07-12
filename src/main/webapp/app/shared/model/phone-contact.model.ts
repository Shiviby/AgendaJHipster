import { IContact } from 'app/shared/model/contact.model';
import { PhoneContactType } from 'app/shared/model/enumerations/phone-contact-type.model';

export interface IPhoneContact {
  id?: number;
  type?: PhoneContactType;
  phone?: string;
  contact?: IContact;
}

export const defaultValue: Readonly<IPhoneContact> = {};
