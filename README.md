# TiveQP
Time-Restricted, Verifiable, and Efficient Query Processing over Encrypted Data on Cloud

---

## Preparing

***We have provided the processed data in each scheme directory***


### Data Downloading

   Download the Yelp dataset from [Yelp](https://www.yelp.com/dataset)

### Data Convertion
   The raw data of Yelp dataset are JSON files, we choose **yelp_academic_dataset_business.json** for the experiment. 

   Run the convertion script **ProcessYelpDataset.py** written in Python3 to extract *city,latitude,longitude,categories,hours* from the JSON file. Befor running the convertion script , please modify the **path** in the script. The output data is 

   **{path}/target.txt**
   
   The converted data is a txt file, the format of each line is 
   *categories\*\*city\*\*latitude\*\*longitude\*\*hour_of_opening_time\*\*minute_of_opening_time\*\*hour_of_closing_time\*\*minute_of_closing_time*
   
   for example *Restaurants\*\*ATLANTA\*\*33.7460355\*\*-84.370798\*\*7\*\*0\*\*20\*\*0*
   
---

## Running with the sample codes

### IBFTree
[IBFTree](https://github.com/UbiPLab/TiveQP/blob/main/TiveQP/IBFTree/README.md)
### PBTree
[PBTree](https://github.com/UbiPLab/TiveQP/blob/main/TiveQP/PBTree/README.md)
### SecEQP
[SecEQP](https://github.com/UbiPLab/TiveQP/blob/main/TiveQP/SecEQP/README.md)
### ServeDB
[ServeDB](https://github.com/UbiPLab/TiveQP/blob/main/TiveQP/ServeDB/README.md)
### TiveQP
[TiveQP](https://github.com/UbiPLab/TiveQP/blob/main/TiveQP/TiveQP/README.md)
### TiveQP_No_Type
[TiveQP_No_Type](https://github.com/UbiPLab/TiveQP/blob/main/TiveQP/TiveQP_NoType/README.md)
