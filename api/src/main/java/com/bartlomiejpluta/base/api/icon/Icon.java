package com.bartlomiejpluta.base.api.icon;

import com.bartlomiejpluta.base.api.entity.Entity;

public interface Icon extends Entity {
   void changeIcon(int row, int column);

   void changeIcon(String iconSetUid, int row, int column);

   String getIconSetUid();

   int getIconSetRow();

   int getIconSetColumn();
}
