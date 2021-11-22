import pandas as pd
from random import randrange

df = pd.read_excel("customers.xlsx", "Foglio1")

out = open('./customers.json', 'w', encoding='utf-8')
out.write('{' + "\n" + "\t" + "\"customers\": [" + "\n")
out.close

for x in range(1,len(df)):
    print(str(df.iloc[x,1]))
    print(str(df.iloc[x,2]))
    print(str(df.iloc[x,4]))
    print(str(df.iloc[x,6]))
    print(str(df.iloc[x,7]).replace(".0",""))
    print(str(df.iloc[x,8]))
    print("----------------------")

    out.write("\t" + "{" + "\n")
    code = str(df.iloc[x,2]).replace(".","")

    if len(code) < 8:
        while len(code) < 8:
            code = code + str("0")

    out.write("\t\t" + "\"code\": " + "\"" + code + "\"" + "," + "\n")
    out.write("\t\t" + "\"name\": " + "\"" + str(df.iloc[x,1]).replace("\"","") + "\"" + "," + "\n")

    if len(str(df.iloc[x,4])) > 13:
        print (str(df.iloc[x,1]))

    cap = str(df.iloc[x,7]).replace(".0","")

    if len(cap) < 5:
        while len(cap) < 5:
            cap = "0" + cap

    out.write("\t\t" + "\"cap\": " + "\"" + cap + "\"" + "," + "\n")
    out.write("\t\t" + "\"city\": " + "\"" + str(df.iloc[x,6]) + "\"" + "," + "\n")
    out.write("\t\t" + "\"province\": " + "\"" + str(df.iloc[x,8]) + "\"" + ","+ "\n")
    
    out.write("\t\t" + "\"discount\": " + "\"" + str(((randrange(4)) * 10)) + "\"" + "\n")
    if x < (len(df) - 1):
        out.write("\t}," + "\n")
    else:
        out.write("\t}" + "\n")

out = open('./customers.json', 'a', encoding='utf-8')
out.write("\t" + "]" + "\n")
out.write("}")
out.close