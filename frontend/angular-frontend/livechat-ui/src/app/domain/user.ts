export interface User {
  userId?: number,
  username: string,
  firstName: string,
  lastName: string,
  phone?: string,
  email: string,
  bio?: string,
  address?: string,
  createdAt?: Date,
  imageUrl?: string,
  lastLoginDateDisplay?: Date
}
