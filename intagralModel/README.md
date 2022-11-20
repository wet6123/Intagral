# model training


## Environment 
--- 

Anaconda3 virtual env  
python 3.9  
ssafy gpu server (tesla V100)  
Yolo V5 (darknet pytorch)  
ClearML mlops solution  
MLflow mlops solution

  
<br> 

## Datasets
---
coco datasets  
pixabay free images
<br>  
<br>  
# usage

### configure training option

in clearml_utils.py,  
configure 'project_name' and 'task_name' of clearml task
```
if self.clearml:
            self.task = Task.init(
                project_name='<project name>',
                task_name='<task name>',
                tags=['YOLOv5','coco128'],
                output_uri=True,
                auto_connect_frameworks={'pytorch': False}
            )   
```  

### sync datasets  
at the dataset directory, enter command 
```  
clearml-data sync --project <project name> --name <task name> --folder .
```  

### start train and logging  


```  
python train.py --img 640 --batch 16 --epochs 50 --data clearml://<dataset id on clearml server> --weights <pretrained model> --cache
```  
