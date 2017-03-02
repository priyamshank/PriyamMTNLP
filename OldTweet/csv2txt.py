# import csv
# with open('csv2txt.csv', 'r') as file:
#     data = file.read().split('\n')
#
# for row in range(1, len(data)):
#     print data[row]
#     third_col = data[row].split('\t')
#     if "Ad" in third_col[8]:
#         with open('t' + str(row) + '.txt', 'w') as output:
#             output.write(third_col[4])
import csv
with open('filename.txt', 'r') as file:
    data = file.read().split('\n')
output = open('OtherTweet.txt','a')

for row in range(1, len(data)):
    print data[row]
    third_col = data[row].split('\t')
    if "others" in third_col[8]:
        # with open('testfile.txt','w') as output:
        output.write(third_col[4] + "\n")