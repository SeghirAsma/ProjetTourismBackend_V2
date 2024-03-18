package com.TourismApp.TourismApplication.Services;

import com.TourismApp.TourismApplication.Models.Item;
import com.TourismApp.TourismApplication.Repositories.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemService {
    @Autowired
    private final ItemRepository itemRepository;
    public ItemService(ItemRepository itemRepository){
        this.itemRepository=itemRepository;
    }

    public List<Item> getAllItems(){
        return itemRepository.findAll();
    }
    public Optional<Item> getItemById(String idItem){
        return itemRepository.findById(idItem);
    }
    public List<Item> getItemsByIds(List<String> itemIds) {
        return itemRepository.findAllById(itemIds);
    }


    public Item createItem(Item item){
        return itemRepository.save(item);
    }

    public ResponseEntity<Item> updateItemById(String id, Item updatedItem) {
        Optional<Item> existingItemOptional = itemRepository.findById(id);

        if (existingItemOptional.isPresent()) {
            Item existingItem = existingItemOptional.get();
            existingItem.setReferenceItem(updatedItem.getReferenceItem());
            existingItem.setName(updatedItem.getName());
            existingItem.setType(updatedItem.getType());
            existingItem.setDestination(updatedItem.getDestination());
            existingItem.setPrice(updatedItem.getPrice());
            existingItem.setDateDebut(updatedItem.getDateDebut());
            existingItem.setDateFin(updatedItem.getDateFin());
            existingItem.setRequired((updatedItem.isRequired()));  //
            Item savedItem = itemRepository.save(existingItem);
            return new ResponseEntity<>(savedItem, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public void deleteItem(String idItem) {
        itemRepository.deleteById(idItem);
    }

    public boolean archiveItem(String idItem){
        if(itemRepository.findById(idItem).isPresent()){
            Item item = itemRepository.findById(idItem).get();
            item.setDeleted(true);
            itemRepository.save(item);
            return  true;
        }
        return  false;
    }

    public List<Item> findArchivedItem(){
        return itemRepository.findByIsDeletedTrue();
    }

    public List<Item> getNonDeletedItems() {
        return itemRepository.findByIsDeletedFalse();
    }


}
