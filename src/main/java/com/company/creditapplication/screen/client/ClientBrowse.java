package com.company.creditapplication.screen.client;

import io.jmix.ui.screen.*;
import com.company.creditapplication.entity.Client;

@UiController("cap_Client.browse")
@UiDescriptor("client-browse.xml")
@LookupComponent("clientsTable")
public class ClientBrowse extends StandardLookup<Client> {
}