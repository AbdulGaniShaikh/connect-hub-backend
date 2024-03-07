package com.shaikhabdulgani.ConnectHub.dto;

import com.shaikhabdulgani.ConnectHub.util.enums.Relation;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RelationWithId {

    /**
     * A enum indicating a relation between two users
     * */
    private Relation relation;
    /**
     * Represents friend request ID, if relation value is FR_RECEIVED or FR_SENT, else empty
     * */
    private String friendRequestId;


    /**
     * Creates a {@link RelationWithId} object with the given request ID indicating a friend request sent.
     *
     * @param requestId The ID of the friend request
     * @return A {@link RelationWithId} object representing the relation with the request ID
     */
    public static RelationWithId friendRequestSent(String requestId){
        return new RelationWithId(Relation.FR_SENT,requestId);
    }

    /**
     * Creates a {@link RelationWithId} object with the given request ID indicating a friend request received.
     *
     * @param requestId The ID of the friend request
     * @return A {@link RelationWithId} object representing the relation with the request ID
     */
    public static RelationWithId friendRequestReceived(String requestId){
        return new RelationWithId(Relation.FR_RECEIVED,requestId);
    }

    /**
     * Creates a {@link RelationWithId} object indicating no relation.
     *
     * @return A {@link RelationWithId} object representing the no relation
     */
    public static RelationWithId none(){
        return new RelationWithId(Relation.NONE,"");
    }

    /**
     * Creates a {@link RelationWithId} object indicating relation as self.
     *
     * @return A {@link RelationWithId} object representing the self relation
     */
    public static RelationWithId self(){
        return new RelationWithId(Relation.SELF,"");
    }

    /**
     * Creates a {@link RelationWithId} object indicating friendship.
     *
     * @return A {@link RelationWithId} object representing the friendship relation
     */
    public static RelationWithId friends(){
        return new RelationWithId(Relation.FRIENDS,"");
    }

}
