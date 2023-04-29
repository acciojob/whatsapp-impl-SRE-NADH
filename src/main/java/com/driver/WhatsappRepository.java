package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class WhatsappRepository {

    //Assume that each user belongs to at most one group
    //You can use the below mentioned hashmaps or delete these and create your own.
    private HashMap<Group, List<User>> groupUserMap;
    private HashMap<Group, List<Message>> groupMessageMap;
    private HashMap<Message, User> senderMap;
    private HashMap<Group, User> adminMap;
    private HashSet<String> userMobile;

    private int customGroupCount;
    private int messageId;

    public WhatsappRepository(){
        this.groupMessageMap = new HashMap<Group, List<Message>>();
        this.groupUserMap = new HashMap<Group, List<User>>();
        this.senderMap = new HashMap<Message, User>();
        this.adminMap = new HashMap<Group, User>();
        this.userMobile = new HashSet<>();
        this.customGroupCount = 0;
        this.messageId = 0;
    }

    public boolean NoAlreadyPresent(String mobile) {
       return userMobile.contains(mobile);
    }

    public void addUser(String name, String mobile) {
        userMobile.add(mobile);
    }

    public Group createpersonalGroup(List<User> users) {
         String name=users.get(1).getName();
         Group group = new Group(name,2);
         groupUserMap.put(group,users);
         adminMap.put(group,users.get(0));
         return group;
    }

    public Group createGroup(List<User> users) {
        customGroupCount++;
        String name="Group "+customGroupCount;
        Group group = new Group(name,users.size());
        groupUserMap.put(group,users);
        return group;
    }

    public int createMessage(String content) {
        messageId++;
        return messageId;
    }

    public boolean GroupisnotPresent(Group group) {
        return groupUserMap.containsKey(group);
    }

    public boolean checkAdmin(Group group,User user) {
        return adminMap.get(group)==user;
    }

    public boolean checkSender(Group group, User sender) {
        List<User> tmp = groupUserMap.get(group);
        for(User x:tmp){
            if(x.equals(sender)){
                return true;
            }
        }
        return false;
    }

    public int sendMessage(Message message, User sender, Group group) {
        sendmessageInsenderuserMap(message,sender);
        if(groupMessageMap.containsKey(group)){
            groupMessageMap.get(group).add(message);
        }
        else{
            List<Message> m = new ArrayList<>();
            m.add(message);
            groupMessageMap.put(group,m);
        }
        return groupMessageMap.get(group).size();
    }

    private void sendmessageInsenderuserMap(Message message, User sender) {
       senderMap.put(message, sender);
    }

    public String addAdmin(Group group, User user) {
        adminMap.put(group,user);
        return "SUCCESS";
    }

    public Optional<Group> finduserGroup(User user) {
        for(Group x:groupUserMap.keySet()){
            for(User y:groupUserMap.get(x)){
                if(y.equals(user)){
                    return Optional.of(x);
                }
            }
        }
        return Optional.empty();
    }

    public int removeUser(Group group, User user) {
        removeFromMessagegroup(group,user);
        removeFromGroupuserMap(group,user);
        return groupMessageMap.get(group).size()+groupUserMap.get(group).size()+senderMap.size();
    }

    private void removeFromMessagegroup(Group group, User user) {
       List<Message> tmp = groupMessageMap.get(group);
       for(Message x:tmp){
           if(senderMap.get(x)==user){
               removeandUpdate(x,group);
               senderMap.remove(x);
           }
       }
    }

    private void removeandUpdate(Message x,Group group) {
        List<Message> tmp = groupMessageMap.get(group);
        tmp.remove(x);
        groupMessageMap.put(group,tmp);
    }

    private void removeFromGroupuserMap(Group group, User user) {
        List<User> tmp = groupUserMap.get(group);
        tmp.remove(user);
        groupUserMap.put(group,tmp);
    }

    public int findMessagesBtwStrtEnd(Date start, Date end) {
        int count=0;
        for(Message x:senderMap.keySet()){
            if(x.getTimestamp().after(start) && x.getTimestamp().before(end)){
                count++;
            }
        }
        return count;
    }

//    public String findMessage(Date start, Date end, int k) {
//        Date max = new Date();
//        for(Message x:senderMap.keySet()){
//            if(x.getTimestamp().after(start) && x.getTimestamp().before(end)){
//                if(max.before(x.getTimestamp())){
//
//                }
//            }
//        }
//    }
}
