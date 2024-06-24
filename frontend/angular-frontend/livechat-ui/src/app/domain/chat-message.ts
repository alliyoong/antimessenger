export interface ChatMessage {
  id?: string,
  chatRoomId: string,
  senderId: number,
  recipientId: number,
  content: string,
  timestamp: Date

}
