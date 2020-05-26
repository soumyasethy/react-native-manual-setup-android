import React from "react";
import { AppRegistry, StyleSheet, Text, View } from "react-native";
import AsyncStorage from "@react-native-community/async-storage";

class HelloWorld extends React.Component {
  render() {
    return (
      <View style={styles.container}>
        <Text style={styles.hello}>Hello, World from React Native</Text>
      </View>
    );
  }
}
var styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: "center"
  },
  hello: {
    fontSize: 20,
    textAlign: "center",
    margin: 10
  }
});

AppRegistry.registerComponent("MyReactNativeApp", () => HelloWorld);
