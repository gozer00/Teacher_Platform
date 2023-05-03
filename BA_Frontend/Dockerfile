#### Stage 1: Build the react application
#FROM node:19-alpine as builder


#WORKDIR /app

# Copy the package.json as well as the package-lock.json and install
# the dependencies. This is a separate step so the dependencies
# will be cached unless changes to one of those two files
# are made.
#COPY package.json package-lock.json ./
#RUN npm install

# Copy the main application
#COPY . ./

#EXPOSE 3000

#CMD ["npm", "start"]

#COPY package.json .
#RUN yarn install
#COPY . .
#RUN jarn build


#FROM nginx:1.19.0
#WORKDIR /usr/share/nginx/html
#RUN rm -f ./*
#COPY --form=builder /app/build .
#ENTRYPOINT ["nginx", "-g", "daemon off;"]