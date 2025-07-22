import pickle
techacademy = ['未経験転職', 'Pythonで人工知能エンジニア', 123456, {'Pyhon': '機械学習'}]
with open('school.binaryfile', 'wb') as web:
  pickle.dump(techacademy , web)

with open('school.binaryfile', 'rb') as web2:
  techacademy2 = pickle.load(web2)
  print (techacademy2)


num_techacademy = len(techacademy)
print (num_techacademy)

with open('school.binaryfile', 'wb') as p_num_techacademy:
  pickle.dump(num_techacademy , p_num_techacademy)

with open('school.binaryfile', 'rb') as p_num_techacademy:
  num_techacademy2 = pickle.load(p_num_techacademy)
  print (num_techacademy2)

