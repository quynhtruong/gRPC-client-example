package com.bynfor.productclient;

import com.bynfor.service.InventoryServiceGrpc;
import com.bynfor.service.InventoryServiceGrpc.InventoryServiceBlockingStub;
import com.bynfor.service.Item;
import com.bynfor.service.ItemList;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class InventoryClient {

  ManagedChannel managedChannel;
  InventoryServiceBlockingStub stub;

  public void updateItems(com.bynfor.productclient.model.Item[] items) { //Item model
    //prepare request data into ItemList
    ItemList itemList = null;
    for (int i = 0; i < items.length; i++) {
      Item item = Item.newBuilder().setCode(items[i].getCode())
          .setQty(items[i].getQty()).build();
      if (itemList != null && itemList.getItemList().size() > 0) {
        itemList = ItemList.newBuilder(itemList).addItem(i, item).build();
      } else {
        itemList = ItemList.newBuilder().addItem(0, item).build();
      }
    }
    //create a channel from server host and port
    managedChannel = ManagedChannelBuilder.forAddress("localhost", 7000).usePlaintext(true).build();
    //create new stub from the channel
    stub = InventoryServiceGrpc.newBlockingStub(managedChannel);
    //call the gRPC
    stub.updateItems(itemList);
  }
}
