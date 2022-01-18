import json
import os
import random

path = "C:\\Users\\UBIPLAB\\Desktop\\Test\\"


def Pre():
    with open(path + "yelp_academic_dataset_business.json", 'r', encoding='utf-8') as f:
        lines = f.readlines()
        length = len(lines)
    with open(path + "yelp_format.json", 'a', encoding='utf-8') as f_format:
        f_format.write("""{"load":[""")
        for count, line in enumerate(lines):
            if count != length - 1:
                f_format.write(line + ",")
            else:
                f_format.write(line)
        f_format.write("]}")


def DataSet_Short():
    with open(path + "yelp_format.json", 'r', encoding='utf-8') as load_f:
        page = json.load(load_f)
    Com = []
    page = page["load"]
    count = 0
    for element in page:
        count += 1
        try:
            cate = element['categories'].split(', ')

            for index in range(0, len(cate)):
                df_dict = {'categories': cate[index], 'city': element['city'], 'state': element['state'],
                           'latitude': element['latitude'],
                           'longitude': element['longitude'],
                           'hours': element['hours']}
                Com.append(df_dict)

        except AttributeError:
            print("NoneType at", count)
    # print(Com)
    with open(path + "Yelp_format_short_cate.json", 'w', encoding="utf-8") as f:
        json.dump(Com, f)


def DataSet_Format():
    with open(path + "yelp_format_short_cate.json", 'r', encoding='utf-8') as load_f:
        page = json.load(load_f)

    Com = []
    city_list = set()
    for element in page:
        element['city'] = element['city'].strip().upper().replace(",", "-").replace(" ", "-")

        df_dict = {'categories': element['categories'], 'city': element['city'], 'state': element['state'],
                   'latitude': element['latitude'],
                   'longitude': element['longitude'],
                   'hours': element['hours']}
        city_list.add(element['city'])
        Com.append(df_dict)
    with open(path + "yelp_use.json", 'w', encoding="utf-8") as f:
        json.dump(Com, f)
    with open(path + "city_use.txt", 'w', encoding='utf-8')as txt:
        for e in city_list:
            txt.write(e + "\n")


def Read_Type():
    with open(path + "yelp_use.json", 'r', encoding='utf-8') as load_f:
        page = json.load(load_f)
    type_list = set()
    for element in page:
        element['categories'] = element['categories'].strip()
        type_list.add(element['categories'])
    with open(path + "type_use.txt", 'w', encoding='utf-8')as txt:
        for e in type_list:
            s = e.replace("/", " or ")
            s = s.replace("&", "and")
            txt.write(s + "\n")


def Classification_Type():


    if not os.path.exists(path + "Format_Type_2"):
        os.makedirs(path + "Format_Type_2")

    with open(path + "yelp_use.json", 'r', encoding='utf-8') as load_f:
        page = json.load(load_f)

    with open(path + "type_use.txt", 'r', encoding="utf-8") as txt:
        type_list = txt.readlines()

    for i in range(0, len(type_list)):
        type_list[i] = type_list[i].replace("\n", "").strip()

        with open(path + "Format_Type_2\\" + type_list[i] + ".txt", 'a', encoding='utf-8')as txt:
            for element in page:
                element['categories'] = element['categories'].strip().replace("/", "&")
                if (element['categories'] == type_list[i]):
                    if (element['hours'] == None):
                        e_hours = "null"
                        continue
                    else:
                        if "Tuesday" in element['hours']:
                            e_hours = element['hours']['Tuesday']
                        elif "Wednesday" in element['hours']:
                            e_hours = element['hours']['Wednesday']
                        elif "Thursday" in element['hours']:
                            e_hours = element['hours']['Thursday']
                        elif "Friday" in element['hours']:
                            e_hours = element['hours']['Friday']
                        elif "Saturday" in element['hours']:
                            e_hours = element['hours']['Saturday']
                        elif "Monday" in element['hours']:
                            e_hours = element['hours']['Monday']
                        elif "Sunday" in element['hours']:
                            e_hours = element['hours']['Sunday']

                        e_hours_split = e_hours.split('-')
                        e_open = e_hours_split[0].replace(":", "**")
                        e_close = e_hours_split[1].replace(":", "**")

                        element['city'] = element['city'].upper()

                        txt.write(element['categories'] + "**" +
                                  element['city'] + "**" +
                                  str(element['latitude']) + "**" +
                                  str(element['longitude']) + "**" +
                                  e_open + "**" + e_close + "\n"
                                  )
        print(i, " ", type_list[i] + " is ok")

    # for i in range(len(type_list)):
    #     class_list=[]
    #     for k in range(len(page)):
    #         if(page[k]['categories'] == type_list[i]):
    #             class_list.append(page[k])
    #     with open(path+"Type\\"+str(i)+" "+type_list[i].replace("\n", "")+"\\"+str(i)+".json",'w',encoding="utf-8")as f:
    #         json.dump(class_list,f)


def Read_City_lat_lng():
    with open(path + "yelp_use.json", 'r', encoding='utf-8') as load_f:
        page = json.load(load_f)
    with open(path + "city_use.txt", 'r', encoding="utf-8") as txt:
        city_list = txt.readlines()

    lat_max = []
    lat_min = []
    lng_max = []
    lng_min = []
    for i in range(0, len(city_list)):
        city_list[i] = city_list[i].replace("\n", "").strip()
        t = []
        g = []
        for element in page:
            element['city'] = element['city'].strip().replace(",", "-").replace(" ", "-").upper()
            if (element['city'] == city_list[i]):
                t.append(element['latitude'])
                g.append(element['longitude'])
            else:
                t.append(0.0)
                g.append(0.0)
        print(i, " ", city_list[i] + " is ok")
        lat_max.append(max(t))
        lat_min.append(min(t))
        lng_max.append(max(g))
        lng_min.append(min(g))

    print(lat_min)
    print(lat_max)

    print(lng_min)
    print(lng_max)


def Merge(number):
    item_list=[]
    for file in os.listdir(path+"Format_Type_2"):
        with open(path + "Format_Type_2\\"+file, 'r', encoding='utf-8')as fobj:
            item_list.extend(fobj.readlines())
    sli=random.sample(item_list,number)
    print(sli)

    with open(path + "target.txt", 'a', encoding='utf-8')as fwobj:
        fwobj.writelines(sli)

def main():
    Pre()
    DataSet_Short()
    DataSet_Format()
    Read_Type()
    Classification_Type()
    Read_City_lat_lng()
    Merge(20000)


if __name__ == '__main__':
    main()
