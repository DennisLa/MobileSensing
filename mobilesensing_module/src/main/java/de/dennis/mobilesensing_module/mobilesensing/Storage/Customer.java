package de.dennis.mobilesensing_module.mobilesensing.Storage;

import io.objectbox.annotation.Backlink;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToMany;

/**
 * Created by Dennis on 29.10.2017.
 */

@Entity
public class Customer {

    @Id
    public long id;

    @Backlink
    public ToMany<Order> orders;

}
