export interface Account{
    accountId?: string,
    username: string,
    firstName: string,
    lastName: string,
    password: string,
    email: string,
    phone?: string,
    bio?: string,
    address?: string,
    lastLoginDateDisplay?: Date,
    role: any,
    isEnabled: string,
    isUsingMfa: string,
    isNonLocked: string,
    createdAt?: Date,
    imageUrl?: string,
    imageFile?: File
}

export interface Role {
    roleId: number,
    roleName: string,
    permission: string
}
