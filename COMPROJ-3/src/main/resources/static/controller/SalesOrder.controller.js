// Developed by Aayush 10-03-2026
sap.ui.define(
    ["sap/ui/core/mvc/Controller",
     "jquery.sap.global",
     "punit/util/service",
     "sap/m/MessageBox"],

    function(Controller, jQuery, service, MessageBox){

        return Controller.extend("punit.controller.SalesOrder", {

            onInit: function(){
                var oModel = new sap.ui.model.json.JSONModel();
                oModel.setData({

                    // SO Header create payload
                    "createHeaderPayload": {
                        "dateOfOrder": null,
                        "dateOfDelivery": null,
                        "customer": { "customerId": null }
                    },

                    // SO Item create payload
                    "createItemPayload": {
                        "itemNumber": null,
                        "material": "",
                        "quantity": null,
                        "uom": "",
                        "salesOrderHeader": { "salesOrderNumber": null }
                    },

                    // SO Item edit payload (only quantity is editable)
                    "editItemPayload": {
                        "soItemId": null,
                        "itemNumber": null,
                        "material": "",
                        "quantity": null,
                        "uom": "",
                        "salesOrderHeader": { "salesOrderNumber": null }
                    },

                    // Read data
                    "salesOrders": [],
                    "salesOrderItems": [],

                    // Search
                    "searchItemId": "",

                    // Flags
                    "editItemMode": false,
                    "showOperationSelector": true,
                    "showCreateSelector":    false,
                    "showCreateHeaderPanel": false,
                    "showCreateItemPanel":   false,
                    "showReadPanel":         false,
                    "showUpdatePanel":       false
                });
                this.getView().setModel(oModel);
            },

            // ─── OPERATION SELECTION ───────────────────────────────────────

            onSelectCreate: function(){
                var oModel = this.getView().getModel();
                oModel.setProperty("/showOperationSelector", false);
                oModel.setProperty("/showCreateSelector",    true);
                oModel.setProperty("/showCreateHeaderPanel", false);
                oModel.setProperty("/showCreateItemPanel",   false);
                oModel.setProperty("/showReadPanel",         false);
                oModel.setProperty("/showUpdatePanel",       false);
            },

            onSelectRead: function(){
                var oModel = this.getView().getModel();
                oModel.setProperty("/showOperationSelector", false);
                oModel.setProperty("/showCreateSelector",    false);
                oModel.setProperty("/showCreateHeaderPanel", false);
                oModel.setProperty("/showCreateItemPanel",   false);
                oModel.setProperty("/showReadPanel",         true);
                oModel.setProperty("/showUpdatePanel",       false);

                // Auto-load both tables
                this.onLoadData();
            },

            onSelectUpdate: function(){
                var oModel = this.getView().getModel();
                oModel.setProperty("/showOperationSelector", false);
                oModel.setProperty("/showCreateSelector",    false);
                oModel.setProperty("/showCreateHeaderPanel", false);
                oModel.setProperty("/showCreateItemPanel",   false);
                oModel.setProperty("/showReadPanel",         false);
                oModel.setProperty("/showUpdatePanel",       true);

                // Reset update state
                oModel.setProperty("/editItemMode", false);
                oModel.setProperty("/searchItemId", "");
            },

            // ─── CREATE SUB-SELECTION ──────────────────────────────────────

            onSelectCreateHeader: function(){
                var oModel = this.getView().getModel();
                oModel.setProperty("/showCreateSelector",    false);
                oModel.setProperty("/showCreateHeaderPanel", true);
                oModel.setProperty("/showCreateItemPanel",   false);
            },

            onSelectCreateItem: function(){
                var oModel = this.getView().getModel();
                oModel.setProperty("/showCreateSelector",    false);
                oModel.setProperty("/showCreateHeaderPanel", false);
                oModel.setProperty("/showCreateItemPanel",   true);
            },

            onBackToCreateSelector: function(){
                var oModel = this.getView().getModel();
                oModel.setProperty("/showCreateSelector",    true);
                oModel.setProperty("/showCreateHeaderPanel", false);
                oModel.setProperty("/showCreateItemPanel",   false);
                this.resetCreateData();
            },

            // ─── BACK / GLOBAL ─────────────────────────────────────────────

            onBack: function(){
                var oModel = this.getView().getModel();
                oModel.setProperty("/showOperationSelector", true);
                oModel.setProperty("/showCreateSelector",    false);
                oModel.setProperty("/showCreateHeaderPanel", false);
                oModel.setProperty("/showCreateItemPanel",   false);
                oModel.setProperty("/showReadPanel",         false);
                oModel.setProperty("/showUpdatePanel",       false);
                this.resetAllData();
            },

            // ─── CREATE SO HEADER ──────────────────────────────────────────

            onSaveHeader: function(){
                var oModel = this.getView().getModel();
                var payload = oModel.getProperty("/createHeaderPayload");

                if(!payload.dateOfOrder || !payload.customer || !payload.customer.customerId){
                    MessageBox.error("Please fill all required fields (Order Date, Customer ID)");
                    return;
                }

                service.callService("/salesorderheader", "POST", payload)
                    .then(function(response){
                        MessageBox.success("SO Header Created Successfully. Order No: " + (response.salesOrderNumber || ""));
                        this.onBackToCreateSelector();
                    }.bind(this))
                    .catch(function(err){
                        MessageBox.error("Error: Failed to create SO Header");
                        console.error(err);
                    });
            },

            // ─── CREATE SO ITEM ────────────────────────────────────────────

            onSaveItem: function(){
                var oModel = this.getView().getModel();
                var payload = oModel.getProperty("/createItemPayload");

                if(!payload.salesOrderHeader || !payload.salesOrderHeader.salesOrderNumber || !payload.itemNumber){
                    MessageBox.error("Please fill all required fields (SO Number, Item Number)");
                    return;
                }

                service.callService("/salesorderitem", "POST", payload)
                    .then(function(response){
                        MessageBox.success("SO Item Created Successfully. Item ID: " + (response.soItemId || ""));
                        this.onBackToCreateSelector();
                    }.bind(this))
                    .catch(function(err){
                        MessageBox.error("Error: Failed to create SO Item");
                        console.error(err);
                    });
            },

            // ─── READ ──────────────────────────────────────────────────────

            onLoadData: function(){
                var that = this;

                // Load SO Headers
                service.callService("/salesorderheader", "GET", {})
                    .then(function(data){
                        var oHeaderTable = that.getView().byId("idHeaderTable");
                        var oModel = that.getView().getModel();
                        oModel.setProperty("/salesOrders", data || []);
                        oHeaderTable.bindRows("/salesOrders");
                    })
                    .catch(function(err){
                        MessageBox.error("Error loading SO Headers");
                        console.error(err);
                    });

                // Load SO Items
                service.callService("/salesorderitem", "GET", {})
                    .then(function(data){
                        var oItemTable = that.getView().byId("idItemTable");
                        var oModel = that.getView().getModel();
                        oModel.setProperty("/salesOrderItems", data || []);
                        oItemTable.bindRows("/salesOrderItems");
                        MessageBox.information("Data loaded successfully");
                    })
                    .catch(function(err){
                        MessageBox.error("Error loading SO Items");
                        console.error(err);
                    });
            },

            // ─── UPDATE SO ITEM ────────────────────────────────────────────

            onSearchItem: function(){
                var oModel = this.getView().getModel();
                var itemId = oModel.getProperty("/searchItemId");

                if(!itemId || String(itemId).trim() === ""){
                    MessageBox.error("Please enter a SO Item ID");
                    return;
                }

                service.callService("/salesorderitem/" + itemId, "GET", {})
                    .then(function(data){

                        // Backend returns empty object when not found
                        if(!data || !data.soItemId){
                            MessageBox.error("This Item no. doesn't exist");
                            oModel.setProperty("/editItemMode", false);
                            return;
                        }

                        oModel.setProperty("/editItemPayload", {
                            "soItemId":   data.soItemId,
                            "itemNumber": data.itemNumber,
                            "material":   data.material,
                            "quantity":   data.quantity,
                            "uom":        data.uom,
                            "salesOrderHeader": data.salesOrderHeader || { "salesOrderNumber": null }
                        });
                        oModel.setProperty("/editItemMode", true);

                    })
                    .catch(function(err){
                        MessageBox.error("This Item no. doesn't exist");
                        oModel.setProperty("/editItemMode", false);
                        console.error(err);
                    });
            },

            onUpdateItem: function(){
                var oModel = this.getView().getModel();
                var editPayload = oModel.getProperty("/editItemPayload");

                if(editPayload.quantity === null || editPayload.quantity === ""){
                    MessageBox.error("Please enter a valid Quantity");
                    return;
                }

                // Backend: PUT /salesorderitem/{id}  — only quantity is updated
                var updatePayload = { "quantity": editPayload.quantity };

                service.callService("/salesorderitem/" + editPayload.soItemId, "PUT", updatePayload)
                    .then(function(){
                        MessageBox.success("SO Item Updated Successfully");
                        this.onBack();
                    }.bind(this))
                    .catch(function(err){
                        MessageBox.error("Error: Failed to update SO Item");
                        console.error(err);
                    });
            },

            onCancelItemEdit: function(){
                var oModel = this.getView().getModel();
                oModel.setProperty("/editItemMode", false);
                oModel.setProperty("/searchItemId", "");
                oModel.setProperty("/editItemPayload", {
                    "soItemId": null,
                    "itemNumber": null,
                    "material": "",
                    "quantity": null,
                    "uom": "",
                    "salesOrderHeader": { "salesOrderNumber": null }
                });
            },

            // ─── RESET HELPERS ─────────────────────────────────────────────

            resetCreateData: function(){
                var oModel = this.getView().getModel();
                oModel.setProperty("/createHeaderPayload", {
                    "dateOfOrder": null,
                    "dateOfDelivery": null,
                    "customer": { "customerId": null }
                });
                oModel.setProperty("/createItemPayload", {
                    "itemNumber": null,
                    "material": "",
                    "quantity": null,
                    "uom": "",
                    "salesOrderHeader": { "salesOrderNumber": null }
                });
            },

            resetAllData: function(){
                this.resetCreateData();
                var oModel = this.getView().getModel();
                oModel.setProperty("/editItemPayload", {
                    "soItemId": null,
                    "itemNumber": null,
                    "material": "",
                    "quantity": null,
                    "uom": "",
                    "salesOrderHeader": { "salesOrderNumber": null }
                });
                oModel.setProperty("/editItemMode", false);
                oModel.setProperty("/searchItemId", "");
            }

        });
    }
);