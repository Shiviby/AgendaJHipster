import { IContact } from 'app/shared/model/contact.model';
import { EmailContactType } from 'app/shared/model/enumerations/email-contact-type.model';

export interface IEmailContact {
  id?: number;
  type?: EmailContactType;
  email?: string;
  contact?: IContact;
}

export const defaultValue: Readonly<IEmailContact> = {};
