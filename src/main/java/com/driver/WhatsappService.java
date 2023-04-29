package com.driver;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public class WhatsappService {

    WhatsappRepository repository = new WhatsappRepository();
    public String createUser(String name, String mobile) throws Exception {
        if(repository.NoAlreadyPresent(mobile)){
            throw new Exception("User already exists");
        }
        repository.addUser(name,mobile);
        return "SUCCESS";
    }

    public Group createGroup(List<User> users) {
        int size = users.size();
        if(size==2){
           return repository.createpersonalGroup(users);
        }
        else {
         return repository.createGroup(users);
        }
        // need an exception if list.size()<2
    }

    public int createMessage(String content) {
           return repository.createMessage(content);
    }

    public int sendMessage(Message message, User sender, Group group) throws Exception {
        if(!repository.GroupisnotPresent(group)){
            throw new Exception("Group does not exist");
        }
        if(!repository.checkSender(group,sender)){
          throw  new Exception("You are not allowed to send message");
        }
        return repository.sendMessage(message,sender,group);
    }

    public String changeAdmin(User approver, User user, Group group) throws Exception {
        if(!repository.GroupisnotPresent(group)){
            throw new Exception("Group does not exist");
        }
        if(!repository.checkAdmin(group,approver)){
            throw new Exception("Approver does not have rights");
        }
        if(!repository.checkSender(group,user)){
            throw new Exception("User is not a participant");
        }
        return repository.addAdmin(group,user);
    }

    public int removeUser(User user) throws Exception {
        Optional<Group> opt = repository.finduserGroup(user);
        if(opt.isEmpty()){
            throw new Exception("User not found");
        }
        if(repository.checkAdmin(opt.get(),user)){
        throw new Exception("Cannot remove admin");
        }
        return repository.removeUser(opt.get(),user);
    }

    public String findMessage(Date start, Date end, int k) throws Exception{
       int n =  repository.findMessagesBtwStrtEnd(start,end);
       if(n<k){
           throw new Exception("K is greater than the number of messages");
       }
       return "not found";
       //return repository.findMessage(start,end,n-k);
    }
}
