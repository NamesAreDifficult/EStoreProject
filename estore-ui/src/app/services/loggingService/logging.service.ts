import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class LoggingService {
  messages: string[] = [];

  add(message: string){
    console.log(message);
    this.messages.push(message);
  }

  clear(){
    this.messages = [];
  }
}
