var PostMaker = React.createClass({
	mixins: [React.addons.LinkedStateMixin],
	types: ['GET', 'SET', 'CALL'],
    getInitialState:function(){
        return {
        	appRoot: 'http://localhost:9091/connector',
            path:'/toronto/apartment/clima/power',
            type:'GET',
            value: '',
            postResponse: undefined
        };
    },
    post: function(){
    	var url = this.state.appRoot;
    	var data = {};
    	if(this.state.path)
    		data.path = this.state.path;
    	if(this.state.type)
    		data.type = this.state.type;
    	if(this.state.value){
    		if(isNaN(this.state.value))
    			data.value = this.state.value;
    		else
    			data.value = parseInt(this.state.value);
    	}
    	
    	$.ajax(url,{
    	    data: JSON.stringify(data),
    	    type: 'POST',
    	    processData: false,
    	    //dataType: "json",
    	    //contentType: 'application/json',
    	    success: function(r){
    	    	var newState = this.state;
    	    	newState.postResponse = r;
    	    	this.setState(newState);
    	    	console.log(r);
    	    }.bind(this)
    	});
    	
    	/*$.post(url, data, function(r){
    		console.log(r);
    	});*/
    },
	render: function(){
		return (<div className='postMaker'>
			<div>Posting to: {this.state.appRoot}</div>
			<div><label htmlFor='appRoot'>App root: </label><input id='appRoot' type="text" valueLink={this.linkState('appRoot')}/></div>
			<div><label htmlFor='path'>Path: </label><input id='path' type="text" valueLink={this.linkState('path')}/></div>
			<div><label htmlFor='types'>Type: </label>
			<select id='types' valueLink={this.linkState('type')}>
				{this.types.map(function(item) {
					return <option value={item}>{item}</option>;
		        }, this)}
			</select></div>
			<div><label htmlFor='dataValue'>Value: </label><input id='dataValue' type="text" valueLink={this.linkState('value')}/></div>
			<div><button onClick={this.post.bind(this)}>SEND</button></div>
			<div>Response: </div>
			<div>{this.state.postResponse}</div>
		</div>);
	}
});