package com.bartlomiejpluta.base.engine.database.service;

import com.bartlomiejpluta.base.engine.common.init.Initializable;
import com.bartlomiejpluta.base.internal.gc.Cleanable;

import java.sql.Connection;

public interface DatabaseService extends Initializable, Cleanable {
   Connection getConnection();
}
