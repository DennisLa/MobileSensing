package de.dennis.mobilesensing_module.mobilesensing.Storage;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToOne;

/**
 * Created by Dennis on 29.10.2017.
 */

@Entity
public class Order {

    @Id
    public long id;

    public ToOne<Customer> customer;

}
