import csv
import numpy as np
from sklearn.preprocessing import LabelEncoder
from sklearn.preprocessing import OneHotEncoder
from sklearn.feature_extraction.text import CountVectorizer
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.cross_validation import train_test_split
from sklearn import metrics
import pydot
from sklearn import tree

with open('CompleteTraningFile.csv', 'r') as csvfile:
    tweetdata_reader = csv.reader(csvfile, delimiter=';', quotechar='"')

    corpus = []
    row = next(tweetdata_reader)
    feature_names = np.array(row)

    # Load dataset, and target classes
    X_Array, y_array = [], []
    for row in tweetdata_reader:
        corpus.append(row[4])
        X_Array.append(row)
        y_array.append(row[7])  # The target value is "AgriTweet"

    X_Array = np.array(X_Array)
    y_array = np.array(y_array)
    print (feature_names, X_Array[0], y_array[0])
    print corpus

X_Array = X_Array[:, [0, 1, 3, 4, 5]]
feature_names = feature_names[[0, 1, 3, 4, 5]]
print (feature_names)
print (X_Array[12], y_array[12])


lbl_enc = LabelEncoder()
lbl_enc.fit(X_Array[:, 0])
print ("Categorical classes:", lbl_enc.classes_)
integer_classes = lbl_enc.transform(lbl_enc.classes_).reshape(15, 1)
print ("Integer classes:", integer_classes)
enc = OneHotEncoder()
one_hot_encoder = enc.fit(integer_classes)
# First, convert clases to 0-(N-1) integers using label_encoder
num_of_rows = X_Array.shape[0]
t = lbl_enc.transform(X_Array[:, 0]).reshape(num_of_rows, 1)
# Second, create a sparse matrix with three columns, each one indicating if the instance belongs to the class
new_features = one_hot_encoder.transform(t)
# Add the new features to titanix_X
X_Array = np.concatenate([X_Array, new_features.toarray()], axis=1)
# Eliminate converted columns
X_Array = np.delete(X_Array, [0], 1)
# Update feature names
feature_names = ['username', 'date', 'retweets', 'hashtags', 'Downy Mildew', 'Early Blight', 'Fusarium',
                 'Head Blight', 'Late Blight', 'Leaf Blight', 'Leaf Curl',
                 'Leaf Rust', 'Leaf spot', 'Powdery Mildew', 'Root rot', 'Septoria',
                 'Snow Mold', 'Yellow Rust']


tfv = TfidfVectorizer()
X_Array = tfv.fit_transform(corpus)
# text_data = list(titanic_X.apply(lambda x: '%s %s %s' % (x[1], x[4], x[5]), axis=1))

X_Array = X_Array.astype(float)
y_array = y_array.astype(float)
X_train, X_test, y_train, y_test = train_test_split(X_Array, y_array, test_size=0.25, random_state=33)

clf = tree.DecisionTreeClassifier(criterion='entropy', max_depth=3,min_samples_leaf=5)
clf = clf.fit(X_train,y_train)


def measure_performance(X, y, clf, show_accuracy=True, show_classification_report=True, show_confusion_matrix=True):
    y_pred = clf.predict(X)
    if show_accuracy:
        print ("Accuracy:{0:.3f}".format(metrics.accuracy_score(y, y_pred)), "\n")

    if show_classification_report:
        print ("Classification report")
        print (metrics.classification_report(y, y_pred), "\n")

    if show_confusion_matrix:
        print ("Confusion matrix")
        print (metrics.confusion_matrix(y, y_pred), "\n")

print "training data results"
measure_performance(X_train, y_train, clf)

clf_dt = tree.DecisionTreeClassifier(criterion='entropy', max_depth=3,min_samples_leaf=5)
clf_dt.fit(X_test,y_test)
print "test data results"
measure_performance(X_test,y_test,clf_dt)
tree.export_graphviz(clf, out_file='tree.dot')
tree.export_graphviz(clf_dt, out_file='testtree.dot')