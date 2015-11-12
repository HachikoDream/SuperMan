package com.ds;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class GreendaoGenerator {
  public static void main(String[] args) throws Exception{
      Schema schema=new Schema(1,"com.ds.greendao");
      addConversation(schema);
      new DaoGenerator().generateAll(schema,"/Users/Wells/AndroidStudioProjects/SuperMan/app/src/main/java-gen");

  }
    private static void addConversation(Schema schema){
        Entity conversation=schema.addEntity("Conversation");
        conversation.addIdProperty();
        conversation.addIntProperty("memberId");
        conversation.addStringProperty("memberName");
        conversation.addDateProperty("chatTime");
        conversation.addStringProperty("memberAvater");
        conversation.addStringProperty("lastContent");
        conversation.addBooleanProperty("isRead");
    }
}
